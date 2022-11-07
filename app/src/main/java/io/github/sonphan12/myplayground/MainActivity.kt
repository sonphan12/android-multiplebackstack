package io.github.sonphan12.myplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.github.sonphan12.myplayground.databinding.ActivityMainBinding
import io.github.sonphan12.myplayground.ui.dashboard.DashboardChildFragment
import io.github.sonphan12.myplayground.ui.dashboard.DashboardFragment
import io.github.sonphan12.myplayground.ui.home.HomeChild2Fragment
import io.github.sonphan12.myplayground.ui.home.HomeChildFragment
import io.github.sonphan12.myplayground.ui.home.HomeFragment
import io.github.sonphan12.myplayground.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                val frag = HomeFragment()
                replace(R.id.nav_host_fragment_activity_main, frag)
            }
        }

        binding.navView.setOnItemSelectedListener {
            selectTabId(it.itemId)
            true
        }
    }

    private val savedBackStackNames = mutableSetOf<String>()

    private fun selectTabId(itemId: Int) {
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val currentName = when (currentFragment) {
            is HomeFragment, is HomeChildFragment, is HomeChild2Fragment -> {
                "home"
            }
            is DashboardFragment, is DashboardChildFragment -> {
                "dashboard"
            }
            else -> if (currentFragment is NotificationsFragment) {
                "notifications"
            } else {
                null
            } ?: return
        }
        val nextName = when (itemId) {
            R.id.navigation_dashboard -> "dashboard"
            R.id.navigation_notifications -> "notifications"
            R.id.navigation_home -> "home"
            else -> null
        } ?: return
        supportFragmentManager.saveBackStack(currentName)
        savedBackStackNames.add(currentName)
        if (savedBackStackNames.contains(nextName)) {
            supportFragmentManager.restoreBackStack(nextName)
            savedBackStackNames.remove(nextName)
        } else {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                val nextFrag = when (nextName) {
                    "dashboard" -> DashboardFragment()
                    "notifications" -> NotificationsFragment()
                    "home" -> HomeFragment()
                    else -> null
                } ?: return
                replace(R.id.nav_host_fragment_activity_main, nextFrag)
                addToBackStack(nextName)
            }
        }
    }

    override fun onBackPressed() {
        val currentName = when (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)) {
            is HomeFragment, is HomeChildFragment, is HomeChild2Fragment -> {
                "home"
            }
            is DashboardFragment -> {
                "dashboard"
            }
            is NotificationsFragment -> {
                "notifications"
            }
            else -> {
                null
            }
        }
        if (currentName == null) {
            super.onBackPressed()
            return
        }
        val backStackEntryByName = (0 until supportFragmentManager.backStackEntryCount)
            .map { supportFragmentManager.getBackStackEntryAt(it) }
            .filter { it.name == currentName }
        if (backStackEntryByName.size > 1 || currentName == "home") {
            super.onBackPressed()
            return
        } else {
            binding.navView.selectedItemId = R.id.navigation_home
        }
    }
}