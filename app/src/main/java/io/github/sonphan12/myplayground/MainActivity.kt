package io.github.sonphan12.myplayground

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.pandora.bottomnavigator.BottomNavigator
import io.github.sonphan12.myplayground.databinding.ActivityMainBinding
import io.github.sonphan12.myplayground.ui.dashboard.DashboardChildFragment
import io.github.sonphan12.myplayground.ui.dashboard.DashboardFragment
import io.github.sonphan12.myplayground.ui.home.HomeChild2Fragment
import io.github.sonphan12.myplayground.ui.home.HomeChildFragment
import io.github.sonphan12.myplayground.ui.home.HomeFragment
import io.github.sonphan12.myplayground.ui.notifications.NotificationsFragment
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var navigator: io.github.sonphan12.myplayground.BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onCreateCustomNavigation(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            navigator.primaryNavFragmentChanged.collect {
                Log.d("SONNEE", "primaryNavFragmentChanged, currentFrag: ${supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main_custom)}")

            }
        }

        lifecycleScope.launchWhenResumed {
            navigator.currentTabId.collect {
                binding.navView.selectedItemId = it
            }
        }

    }

    private fun onCreateCustomNavigation(savedInstanceState: Bundle?) {

        binding.navHostFragmentActivityMainCustom.isVisible = true
        binding.navHostFragmentActivityMain.isVisible = false

        binding.navView.setOnItemSelectedListener {
            navigator.onNavigationItemSelected(it)
            true
        }

        navigator = BottomNavigator(
            containerId = R.id.nav_host_fragment_activity_main_custom,
            fragmentManager = supportFragmentManager,
            rootFragmentFactory = mapOf(
                R.id.navigation_home to FragmentFactoryWithTagString({ HomeFragment() }, "home"),
                R.id.navigation_dashboard to FragmentFactoryWithTagString(
                    { DashboardFragment() },
                    "dashboard"
                ),
                R.id.navigation_notifications to FragmentFactoryWithTagString(
                    { NotificationsFragment() },
                    "notifications"
                )
            ),
            defaultTabId = R.id.navigation_home
        ).also {
            it.onCreate(savedInstanceState)
        }
    }


    override fun onBackPressed() {
        if (!navigator.pop()) {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navigator.onSaveInstanceState(outState)
    }
}