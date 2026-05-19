package com.virasat.nammaguide.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.virasat.nammaguide.R
import com.virasat.nammaguide.ui.screens.PassportScreen
import com.virasat.nammaguide.ui.screens.QRScannerScreen
import com.virasat.nammaguide.ui.screens.SiteDetailScreen
import com.virasat.nammaguide.ui.screens.SiteDiscoveryScreen
import com.virasat.nammaguide.ui.theme.AgedParchment
import com.virasat.nammaguide.ui.theme.SandstoneGold
import com.virasat.nammaguide.ui.theme.TempleMaroon
import com.virasat.nammaguide.ui.theme.WarmOffWhite

// ── Routes ────────────────────────────────────────────────────────────────────
sealed class Screen(val route: String, val labelRes: Int, val icon: ImageVector) {
    object Discovery : Screen("discovery",  R.string.nav_discover,  Icons.Outlined.Explore)
    object Scanner   : Screen("scanner",    R.string.nav_qr_scan,   Icons.Outlined.QrCodeScanner)
    object Passport  : Screen("passport",   R.string.nav_passport,  Icons.Outlined.MenuBook)
}

private val bottomNavScreens = listOf(Screen.Discovery, Screen.Scanner, Screen.Passport)
private const val DETAIL_ROUTE = "detail/{siteId}"

// ── Root composable ───────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirasatApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    // Hide bottom bar on detail screen
    val showBottomBar = currentDest?.route != DETAIL_ROUTE

    Scaffold(
        containerColor = AgedParchment,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = WarmOffWhite,
                    tonalElevation = NavigationBarDefaults.Elevation
                ) {
                    bottomNavScreens.forEach { screen ->
                        val selected = currentDest?.hierarchy
                            ?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = stringResource(screen.labelRes)
                                )
                            },
                            label = { Text(stringResource(screen.labelRes)) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = TempleMaroon,
                                selectedTextColor = TempleMaroon,
                                indicatorColor = AgedParchment,
                                unselectedIconColor = SandstoneGold,
                                unselectedTextColor = SandstoneGold
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Discovery.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Discovery.route) {
                SiteDiscoveryScreen(
                    onSiteClick = { siteId ->
                        navController.navigate("detail/$siteId")
                    }
                )
            }

            composable(
                route = DETAIL_ROUTE,
                arguments = listOf(navArgument("siteId") { type = NavType.StringType })
            ) { backStack ->
                val siteId = backStack.arguments?.getString("siteId") ?: ""
                SiteDetailScreen(
                    siteId = siteId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Scanner.route) {
                QRScannerScreen(
                    onSiteFound = { siteId ->
                        navController.navigate("detail/$siteId") {
                            // Don't stack multiple detail screens from scanner
                            popUpTo(Screen.Scanner.route) { inclusive = false }
                        }
                    }
                )
            }

            composable(Screen.Passport.route) {
                PassportScreen()
            }
        }
    }
}
