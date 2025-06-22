package com.metafortech.calma.presentation.authentication.interest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.BackButton
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.LoadingStateIndicator
import com.metafortech.calma.presentation.NextButton

@Composable
fun InterestSelectionScreen(
    modifier: Modifier = Modifier,
    state: InterestScreenUiState,
    onBackClick: () -> Unit,
    onInterestSelected: (Int) -> Unit,
    selectedLang: String,
    onNextClick: () -> Unit,
) {
    LoadingStateIndicator(state.isLoading) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            BackButton { onBackClick() }

            Text(
                text = stringResource(R.string.select_interest),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
            ErrorStateIndicator(error = state.error, onRetry = null)

            LazyVerticalGrid(
                modifier = Modifier.weight(1f),
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                items(state.interests) { interest ->
                    val isSelected = interest.id == state.selectedInterestId
                    InterestItem(
                        interest = if (selectedLang == "ar") {
                            interest.interestNameAr
                        } else if (selectedLang == "en") {
                            interest.interestNameEn
                        } else {
                            interest.interestNameFr
                        },
                        isSelected = isSelected,
                        onClick = { onInterestSelected(interest.id) }
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
                    enabled = state.selectedInterestId!=null
                ) {
                    onNextClick()
                }
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
        Color(0xFFFCE4EC)
    } else {
        Color(0xFFF5F5F5)
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
            .clickable(onClick = { onClick() }),
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
