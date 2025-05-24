package com.metafortech.calma.presentation.home.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.LoadingUserCircularImage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onCreateNewPostClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LoadingUserCircularImage(imageUrl = state.userImageUrl)
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.userName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.clickable(onClick = { onCreateNewPostClick() })
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.what_is_new_in_your_sports_Journey),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0x65000000)
                )
                Row(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.live_gray),
                        stringResource(R.string.record_video)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.camera),
                        stringResource(R.string.take_photo)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.voice_mail),
                        stringResource(R.string.record_audio)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.gallery),
                        stringResource(R.string.select_from_gallery)
                    )

                }
            }
        }
    }
}

@Composable
fun CreatePostOptionsItem(painter: Painter, description: String) {
    Image(
        modifier = Modifier.size(18.dp),
        painter = painter,
        contentDescription = description
    )
}