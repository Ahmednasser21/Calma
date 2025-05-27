package com.metafortech.calma.presentation.home

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.metafortech.calma.presentation.AppRoute
import com.metafortech.calma.presentation.AppRoute.HomeNav
import com.metafortech.calma.presentation.AppRoute.HomeScreen
import com.metafortech.calma.presentation.AppRoute.ChattingScreen
import com.metafortech.calma.presentation.AppRoute.ReelsScreen
import com.metafortech.calma.presentation.AppRoute.SportsFacilitiesScreen
import com.metafortech.calma.presentation.AppRoute.StoreScreen
import com.metafortech.calma.presentation.home.home.HomeScreen
import com.metafortech.calma.presentation.home.home.HomeViewModel
import com.metafortech.calma.presentation.home.home.SamplePosts

fun NavGraphBuilder.homeNav(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    navigation<HomeNav>(startDestination = HomeScreen) {

        composable<HomeScreen> { backStackEntry ->

            val homeViewModel: HomeViewModel = hiltViewModel()
            val state = homeViewModel.homeState.collectAsStateWithLifecycle().value

            HomeScreen(
                modifier = Modifier.padding(innerPadding), state = state
            )
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
    navController: NavHostController,
    userImageUrl: String,
    isLoggedIn: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val route = currentBackStackEntry?.destination?.route
    val routeAndScreenTitle = isHomeNavigationRoute(route)
    val isHomeNavigation = if (route != null) {
        routeAndScreenTitle.first
    } else isLoggedIn

    if (isHomeNavigation) {
        val homeNavEntry = remember(currentBackStackEntry) {
            runCatching { navController.getBackStackEntry(route ?: "") }.getOrNull()
        }
        val homeArgs = homeNavEntry?.toRoute<HomeNav>()

        HomeScaffold(
            userImageUrl = homeArgs?.userImageUrl ?: userImageUrl,
            screenTitle = null,
            currentRoute = route,
            onNavigationItemSelected = { targetRoute ->
                navController.navigate(targetRoute) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            content = { paddingValues ->
                content(paddingValues)
            }
        )
    } else {
        SimpleScaffold { paddingValues ->
            content(paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScaffold(
    userImageUrl: String,
    screenTitle: String? = null,
    currentRoute: String?,
    onNavigationItemSelected: (AppRoute) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                userImageUrl = userImageUrl,
                screenTitle = screenTitle,
                notificationCount = 5,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = currentRoute,
                onItemSelected = onNavigationItemSelected
            )
        },
        content = content
    )
}


@Composable
private fun SimpleScaffold(
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        content = content
    )
}


@Composable
fun rememberAppInitState(): Triple<Boolean, Boolean, String> {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }
    val isRegistered = remember { prefs.getBoolean("is_registered", false) }
    val isLoggedIn = remember { prefs.getBoolean("is_loggedIn", false) }
    val userImageUrl = remember {
        prefs.getString(
            "ImageUrl",
            "https://www.iconpacks.net/icons/1/free-user-icon-972-thumb.png"
        ) ?: "https://www.iconpacks.net/icons/1/free-user-icon-972-thumb.png"
    }
    return Triple(isRegistered, isLoggedIn, userImageUrl)
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

fun isHomeNavigationRoute(route: String?): Pair<Boolean, String?> {
    val screenNames = listOf(
        "HomeScreen",
        "SportsFacilitiesScreen",
        "ReelsScreen",
        "StoreScreen",
        "ChattingScreen"
    )

    val matchedScreenName = screenNames.find { screenName -> route?.contains(screenName) == true }

    return Pair(matchedScreenName != null, matchedScreenName)
}