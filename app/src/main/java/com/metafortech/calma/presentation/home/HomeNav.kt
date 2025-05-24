package com.metafortech.calma.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.metafortech.calma.presentation.AppRoute.HomeNav
import com.metafortech.calma.presentation.AppRoute.HomeScreen
import com.metafortech.calma.presentation.AppRoute.ChattingScreen
import com.metafortech.calma.presentation.AppRoute.ReelsScreen
import com.metafortech.calma.presentation.AppRoute.SportsFacilitiesScreen
import com.metafortech.calma.presentation.AppRoute.StoreScreen
import com.metafortech.calma.presentation.home.home.HomeScreen
import com.metafortech.calma.presentation.home.home.HomeViewModel

fun NavGraphBuilder.homeNav(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    navigation<HomeNav>(startDestination = HomeScreen) {

        composable<HomeScreen> { backStackEntry ->

            val homeViewModel: HomeViewModel = hiltViewModel()
            val state = homeViewModel.homeState.collectAsStateWithLifecycle().value

            HomeScreen(modifier = Modifier.padding(innerPadding), state = state)
        }
        composable<SportsFacilitiesScreen> {
            SportsFacilitiesScreen(modifier = Modifier.padding(innerPadding))
        }

        composable<ReelsScreen> {
            ReelsScreen(modifier = Modifier.padding(innerPadding))
        }

        composable<StoreScreen> {
            StoreScreen(modifier = Modifier.padding(innerPadding))
        }

        composable<ChattingScreen> {
            ChatScreen(modifier = Modifier.padding(innerPadding))
        }
    }

}

@Composable
fun ConditionalScaffold(
    isHomeNavigation: Boolean,
    navController: NavHostController,
    userImageUrl: String,
    content: @Composable (PaddingValues) -> Unit
) {
    if (isHomeNavigation) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        val route = currentBackStackEntry?.destination?.route
        val homeNavEntry =
            remember(currentBackStackEntry) { navController.getBackStackEntry<HomeNav>() }
        val homeArgs = homeNavEntry.toRoute<HomeNav>()
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                TopBar(
                    userImageUrl = homeArgs.userImageUrl
                        ?: userImageUrl,
                    notificationCount = 5
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedRoute = route,
                    onItemSelected = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    } else {
        Scaffold { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
fun SportsFacilitiesScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("SportsFacilities Screen")
    }
}

@Composable
fun ReelsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Reels Screen")
    }
}

@Composable
fun StoreScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Store Screen")
    }
}

@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Chat Screen")
    }
}


