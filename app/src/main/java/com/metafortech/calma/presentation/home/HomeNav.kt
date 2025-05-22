package com.metafortech.calma.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.metafortech.calma.presentation.home.home.HomeScreen
import kotlinx.serialization.Serializable


fun NavGraphBuilder.homeNav(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    navigation<HomeNav>(startDestination = HomeScreen) {

        composable<HomeScreen> {
            HomeScreen(modifier = Modifier.padding(innerPadding))
        }

    }
}

@Composable
fun ConditionalScaffold(
    isHomeNavigation: Boolean,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    if (isHomeNavigation) {
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                TopBar(
                    userImageUrl = "https://i.pinimg.com/736x/a0/06/fb/a006fb4e67553b035f355e08c144595b.jpg",
                    notificationCount = 5
                )
            },
            bottomBar = {
                Text(text = "Home")
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


@Serializable
object HomeNav

@Serializable
object HomeScreen