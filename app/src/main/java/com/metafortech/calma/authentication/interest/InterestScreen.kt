package com.metafortech.calma.authentication.interest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.authentication.NextButton

@Composable
fun InterestSelectionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onInterestSelected: (String) -> Unit,
    onNextClick: () -> Unit,
    selectedInterest: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Text(
            text = stringResource(R.string.select_interest),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        val interests = listOf(
            stringResource(R.string.trainer),
            stringResource(R.string.referre),
            stringResource(R.string.therapist),
            stringResource(R.string.commentator),
            stringResource(R.string.nutritionist),
            stringResource(R.string.analyst),
            stringResource(R.string.player),
            stringResource(R.string.commenter)
        )

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items(interests) { interest ->
                val isSelected = interest == selectedInterest
                InterestItem(
                    interest = interest,
                    isSelected = isSelected,
                    onClick = { onInterestSelected(interest) }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            NextButton(
                modifier = Modifier.padding(bottom = 64.dp),
            ) {
                onNextClick()
            }
        }
    }
}

@Composable
fun InterestItem(
    interest: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Color(0xFFFCE4EC) // Light pink background when selected
    } else {
        Color(0xFFF5F5F5) // Light gray background
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        Color.LightGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = interest,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
    }
}

