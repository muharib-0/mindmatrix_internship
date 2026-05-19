package com.virasat.nammaguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.virasat.nammaguide.databinding.ActivityMainBinding

/**
 * Single-activity host for the entire app.
 * Jetpack Navigation Component drives all fragment transactions.
 * The bottom navigation bar connects to the nav graph's top-level destinations.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar
        setSupportActionBar(binding.toolbar)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Top-level destinations (no back button shown in toolbar)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.siteDiscoveryFragment,
                R.id.qrScannerFragment,
                R.id.passportFragment
            )
        )

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
