package com.example.progr3ssapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.progr3ssapp.R
import com.example.progr3ssapp.ui.auth.LoginScreen
import com.example.progr3ssapp.ui.auth.RegisterScreen
import com.example.progr3ssapp.ui.home.HomeScreen
import com.example.progr3ssapp.ui.profile.ProfileScreen

private data class BottomNavItem(
    val route: Any,
    val label: String,
    val icon: Int
)

private val bottomNavItems = listOf(
    BottomNavItem(route = Home, label = "Főoldal", icon = R.drawable.ic_home),
    BottomNavItem(route = Profile(), label = "Profil", icon = R.drawable.ic_profile)
)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // Az alsó menü csak a MainGraph-on belül látszik (Login/Register alatt rejtve)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.any {
        it.hasRoute(MainGraph::class)
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    // A Home a MainGraph kezdőképernyője: így a Vissza gomb
                                    // a Profil fülről a Home-ra lép, nem zárja be az appot
                                    popUpTo<Home> { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(painterResource(item.icon), contentDescription = null) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(MainGraph) {
                            popUpTo<Login> { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Register) }
                )
            }

            composable<Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        // A Login (és a fölötte lévő Register) is lekerül a back stackről
                        navController.navigate(MainGraph) {
                            popUpTo<Login> { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            navigation<MainGraph>(startDestination = Home) {
                composable<Home> {
                    HomeScreen(
                        onNavigateToProfile = { userId, userName ->
                            navController.navigate(Profile(userId = userId, userName = userName))
                        }
                    )
                }

                composable<Profile> { backStackEntry ->
                    val profile: Profile = backStackEntry.toRoute()
                    ProfileScreen(userId = profile.userId, userName = profile.userName)
                }
            }
        }
    }
}
