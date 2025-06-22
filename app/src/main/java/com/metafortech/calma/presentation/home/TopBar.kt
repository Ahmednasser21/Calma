package com.metafortech.calma.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.UserCircularImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    notificationCount: Int,
    onNotificationClick: () -> Unit = {},
    onUserProfileClick: () -> Unit,
    onLiveClick: () -> Unit = {},
    onEdificesClick: () -> Unit = {},
    onChampionsClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    screenTitle: String? = null
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            title = {
                Text(
                    text = screenTitle ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            navigationIcon = {
                UserCircularImage(userImageUrl)
            },
            actions = {
                Box {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(R.drawable.notification_bing),
                            contentDescription = stringResource(R.string.notification),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    if (notificationCount > 0) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset((-4).dp, -(12).dp)
                                .size(18.dp)
                                .clip(CircleShape),
                            containerColor = Color(0xFFFF0000)
                        ) {
                            Text(
                                modifier = Modifier
                                    .offset(0.dp, (-4).dp)
                                    .align(Alignment.CenterVertically),
                                text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                }

                IconButton(onClick = onUserProfileClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.user_profile),
                        contentDescription = stringResource(R.string.user_profile)
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )

        NavigationTopBar(
            onLiveClick = onLiveClick,
            onEdificesClick = onEdificesClick,
            onChampionshipsClick = onChampionsClick,
            onFriendsClick = onFriendsClick
        )

        HorizontalDivider(thickness = 1.dp)
    }
}


@Composable
fun NavigationTopBar(
    onLiveClick: () -> Unit = {},
    onEdificesClick: () -> Unit = {},
    onChampionshipsClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NavigationItem(
            text = stringResource(R.string.live),
            icon = painterResource(R.drawable.live),
            onClick = onLiveClick
        )

        NavigationItem(
            text = stringResource(R.string.edifices),
            icon = painterResource(R.drawable.groups),
            onClick = onEdificesClick
        )

        NavigationItem(
            text = stringResource(R.string.championships),
            icon = painterResource(R.drawable.championship),
            onClick = onChampionshipsClick
        )

        NavigationItem(
            text = stringResource(R.string.friends),
            icon = painterResource(R.drawable.friends),
            onClick = onFriendsClick
        )

    }
}

@Composable
private fun NavigationItem(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    radius = 36.dp,
                    color = MaterialTheme.colorScheme.secondary
                ),
            ) { onClick() }
    ) {

        Icon(
            painter = icon,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
