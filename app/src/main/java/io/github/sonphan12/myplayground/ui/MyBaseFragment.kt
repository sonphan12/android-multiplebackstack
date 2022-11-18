package io.github.sonphan12.myplayground.ui

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment

abstract class MyBaseFragment : Fragment() {
//    private var pendingIgnoreEnterAnim = false
//
//    private var pendingIgnoreExitAnim = false
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SONNE", "onCreate: $this, $savedInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SONNE", "onDestroy: $this")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SONNE", "onViewCreated: $this\n$savedInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Log.d("SONNE", "onAttached: $this")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("SONNE", "onDetached: $this")
    }

    //
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("SONNE", "onSaveInstanceState: $this")
    }
//
//    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
//        var ignoreAnim = false
//        if (enter && pendingIgnoreEnterAnim) {
//            pendingIgnoreEnterAnim = false
//            ignoreAnim = true
//        } else if (!enter && pendingIgnoreExitAnim) {
//            pendingIgnoreExitAnim = false
//            ignoreAnim = true
//        }
//        return if (ignoreAnim) {
//            noAnimation
//        } else {
//            super.onCreateAnimation(transit, enter, nextAnim)
//        }
//    }

    companion object {
        private val noAnimation: Animation = object : Animation() {}.also {
            it.duration = 0
        }
    }
}