package com.metafortech.calma.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil3.compose.AsyncImage
import com.metafortech.calma.R
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
    content: @Composable (PaddingValues) -> Unit
) {
    if (isHomeNavigation) {
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                TopBar(
                    imageURL = "https://i.pinimg.com/736x/a0/06/fb/a006fb4e67553b035f355e08c144595b.jpg",
                    notificationCount = 5,
                    onNotificationClick = {}
                )
            },
            bottomBar = { /*HomeBottomNavigationBar(navController)*/
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

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    imageURL: String = "",
    notificationCount: Int = 5,
    onNotificationClick: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(60.dp)
                    .clip(CircleShape),
                placeholder = painterResource(R.drawable.outline_person_24),
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(1f))

            BadgedBox(
                modifier = Modifier.clickable(
                    onClick = onNotificationClick
                ),
                badge = {
                    if (notificationCount > 0) {
                        Badge(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.Center)
                        ) {
                            Text(
                                text = notificationCount.toString(),
                                fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                            )
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(32.dp).padding(horizontal = 16.dp),
                )
            }
            Image(
                painter = painterResource(R.drawable.user_profile),
                contentDescription = "Profile",
                modifier = Modifier.size(32.dp).padding(horizontal = 16.dp)
            )
        }

    }
}

@Serializable
object HomeNav

@Serializable
object HomeScreen