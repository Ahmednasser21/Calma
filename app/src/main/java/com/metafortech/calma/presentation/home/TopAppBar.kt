package com.metafortech.calma.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.metafortech.calma.R
import com.metafortech.calma.presentation.LoadingUserCircularImage

@Composable
fun TopBar(
    userImageUrl: String,
    notificationCount: Int,
    onNotificationClick: () -> Unit = {},
    onUserProfileClick: () -> Unit = {},
    onLiveClick: () -> Unit = {},
    onEdificesClick: () -> Unit = {},
    onChampionsClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {}
) {
    Column {
        UserTopBar(
            imageUrl = userImageUrl,
            notificationCount = notificationCount,
            onNotificationClick = {onNotificationClick()},
            onUserProfileClick = {onUserProfileClick()}
        )
        NavigationTopBar(
            onLiveClick = { onLiveClick() },
            onEdificesClick = {onEdificesClick()},
            onChampionshipsClick = {onChampionsClick()},
            onFriendsClick = {onFriendsClick()}
        )
        HorizontalDivider(
            thickness = 1.dp
        )

    }
}

@Composable
fun UserTopBar(
    imageUrl: String,
    notificationCount: Int,
    onNotificationClick: () -> Unit = {},
    onUserProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(top = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadingUserCircularImage(imageUrl)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onNotificationClick) {
            Icon(
                painter = painterResource(R.drawable.notification_bing),
                contentDescription = stringResource(R.string.notification),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
            if (notificationCount > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .offset((-10).dp, 6.dp)
                        .size(16.dp),
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        text = if (notificationCount > 9) "9+" else notificationCount.toString(),
                        fontSize = 8.sp,
                        color = Color.White,
                        modifier = Modifier.offset(0.dp, (-4.5).dp)
                    )
                }
            }
        }

        IconButton(onClick = onUserProfileClick) {
            Icon(
                painter = painterResource(id = R.drawable.user_profile),
                contentDescription = stringResource(R.string.user_profile),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

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
            text = stringResource(R.string.friends),
            icon = painterResource(R.drawable.friends),
            onClick = onFriendsClick
        )

        NavigationItem(
            text = stringResource(R.string.championships),
            icon = painterResource(R.drawable.championship),
            onClick = onChampionshipsClick
        )
        NavigationItem(
            text = stringResource(R.string.edifices),
            icon = painterResource(R.drawable.groups),
            onClick = onEdificesClick
        )

        NavigationItem(
            text = stringResource(R.string.live),
            icon = painterResource(R.drawable.live),
            onClick = onLiveClick
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
            .clickable { onClick() }
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
