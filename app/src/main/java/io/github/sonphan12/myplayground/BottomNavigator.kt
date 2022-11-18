package io.github.sonphan12.myplayground

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import java.util.*

class BottomNavigator(
    @IdRes private val containerId: Int,
    private val fragmentManager: FragmentManager,
    private val rootFragmentFactory: Map<Int, FragmentFactoryWithTagString>,
    private val defaultTabId: Int,
) {
    private val _currentTabId = MutableStateFlow(-1)
    val currentTabId = _currentTabId.asStateFlow()

    private var tabStackMap = StackOfStacks()

    private val _primaryNavFragmentChanged = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val primaryNavFragmentChanged = _primaryNavFragmentChanged.asSharedFlow()

    fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            switchTab(defaultTabId)
        } else {
            _currentTabId.value = savedInstanceState.getInt(KEY_CURRENT_TAB_ID, -1)
            tabStackMap = savedInstanceState.getParcelable(KEY_STACKS) ?: StackOfStacks()
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_CURRENT_TAB_ID, _currentTabId.value)
        outState.putParcelable(KEY_STACKS, tabStackMap)
    }


    @Parcelize
    private data class ParcelablePair(val first: Parcelable, val second: ArrayList<Parcelable>) :
        Parcelable

    fun onNavigationItemSelected(menuItem: MenuItem) {
        val tab = menuItem.itemId
        if (_currentTabId.value != tab) {
            switchTab(tab)
        } else {
            // TODO: Handle reselect
        }
    }

    fun switchTab(tabId: Int) {
        if (tabId == _currentTabId.value) {
            return
        }
        _currentTabId.value = tabId
        if (tabStackMap.stackExists(tabId)) {
            tabStackMap.moveToTop(tabId)
            val tag = tabStackMap.peekValue()!!
            val fragment = fragmentManager.findFragmentByTag(tag.toString())
            showExistedFragment(fragment!!)
        } else {
            val rootFragment = rootFragmentFactory[tabId]!!
            openFragmentInCurrentTab(
                rootFragment.fragmentFactory(), rootFragment.tag,
                0,
                0, 0, 0
            )
        }
    }

    fun openFragmentInCurrentTab(
        fragment: Fragment,
        tag: String,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int
    ) {
        if (fragmentManager.isStateSaved) {
            return
        }
        val internalTag = InternalTag.create(tag, enterAnim, exitAnim, popEnterAnim, popExitAnim)
        tabStackMap.push(_currentTabId.value, internalTag)
        val currentFragment = fragmentManager.primaryNavigationFragment
        fragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                enterAnim,
                exitAnim,
                popEnterAnim,
                popExitAnim,
            )
            currentFragment?.let {
                detach(it)
            }
            add(containerId, fragment, internalTag.toString())
            setPrimaryNavigationFragment(fragment)
            runOnCommit {
                _primaryNavFragmentChanged.tryEmit(Unit)
            }
        }
    }

    fun pop(): Boolean {
        val popped = tabStackMap.pop()!!
        val peek = tabStackMap.peek()
        return if (peek == null) {
            if (_currentTabId.value == defaultTabId) {
                false
            } else {
                switchTab(defaultTabId)
                true
            }
        } else {
            val (tab, nextFragmentTag) = peek
            _currentTabId.value = tab
            val currentFragment = fragmentManager.primaryNavigationFragment
            val nextFragment = fragmentManager.findFragmentByTag(nextFragmentTag.toString())
            fragmentManager.commit {
                setReorderingAllowed(true)
                setCustomAnimations(
                    popped.transitionData.popEnterAnim,
                    popped.transitionData.popExitAnim
                )
                attach(nextFragment!!)
                currentFragment?.let {
                    remove(it)
                }
                setPrimaryNavigationFragment(nextFragment)
                runOnCommit {
                    _primaryNavFragmentChanged.tryEmit(Unit)
                }
            }
            true
        }
    }

    private fun showExistedFragment(fragment: Fragment) {
        if (fragmentManager.isStateSaved) {
            return
        }
        val currentFragment = fragmentManager.primaryNavigationFragment
        fragmentManager.commit {
            setReorderingAllowed(true)
            currentFragment?.let {
                detach(it)
            }
            attach(fragment)
            setPrimaryNavigationFragment(fragment)
            runOnCommit {
                _primaryNavFragmentChanged.tryEmit(Unit)
            }
        }
    }

    companion object {
        private const val KEY_CURRENT_TAB_ID = "KEY_CURRENT_TAB_ID"
        private const val KEY_STACKS = "KEY_STACKS"
    }
}

internal data class InternalTag(
    val tag: String,
    val uuid: String,
    val transitionData: TransitionData,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(TransitionData::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(tag)
        parcel.writeString(uuid)
        parcel.writeParcelable(transitionData, flags)
    }

    override fun describeContents(): Int = 0

    companion object {
        fun create(
            tag: String,
            enterAnim: Int,
            exitAnim: Int,
            popEnterAnim: Int,
            popExitAnim: Int
        ) = InternalTag(
            tag = tag,
            uuid = UUID.randomUUID().toString(),
            transitionData = TransitionData(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        )

        @SuppressWarnings("UnusedDeclaration")
        @JvmField
        val CREATOR = object : Parcelable.Creator<InternalTag> {
            override fun createFromParcel(parcel: Parcel): InternalTag {
                return InternalTag(parcel)
            }

            override fun newArray(size: Int): Array<InternalTag?> {
                return arrayOfNulls(size)
            }
        }

    }

    internal data class TransitionData(
        val enterAnim: Int,
        val exitAnim: Int,
        val popEnterAnim: Int,
        val popExitAnim: Int
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(enterAnim)
            parcel.writeInt(exitAnim)
            parcel.writeInt(popEnterAnim)
            parcel.writeInt(popExitAnim)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<TransitionData> {
            override fun createFromParcel(parcel: Parcel): TransitionData {
                return TransitionData(parcel)
            }

            override fun newArray(size: Int): Array<TransitionData?> {
                return arrayOfNulls(size)
            }
        }
    }
}
