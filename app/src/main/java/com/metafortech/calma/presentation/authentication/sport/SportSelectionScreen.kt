package com.metafortech.calma.presentation.authentication.sport

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.BackButton
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.ImageLoading
import com.metafortech.calma.presentation.LoadingStateIndicator
import com.metafortech.calma.presentation.NextButton

@Composable
fun SportSelectionScreen(
    modifier: Modifier = Modifier,
    state: SportSelectionUiState,
    selectedLang: String,
    selectSport: (Int) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onUpdatingSportAndInterestSuccess: @Composable () -> Unit
) {
    LoadingStateIndicator(isLoading = state.isLoading) {

        onUpdatingSportAndInterestSuccess()

        Box(modifier = modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BackButton { onBackClick() }

                Text(
                    text = stringResource(R.string.select_sport_type_you_prefer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                ErrorStateIndicator(
                    error = state.error,
                    onRetry = null
                )

                if (state.sports.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = Modifier.weight(1f),
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.sports) { sport ->
                            SportItem(
                                sport = sport,
                                selectedLang = selectedLang,
                                isSelected = sport.id == state.selectedSportId,
                                onClick = {
                                    selectSport(sport.id)
                                }
                            )
                        }
                    }
                }

                NextButton(enabled = state.selectedSportId!= null) { onNextClick() }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SportItem(
    sport: Sport,
    isSelected: Boolean,
    selectedLang: String,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        Color.Transparent
    }

    val borderWidth = if (isSelected) 2.dp else 0.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(onClick = onClick)
        ) {
            ImageLoading(
                imageURL = sport.imageUrl,
                modifier = Modifier.fillMaxSize(),
                contentDescription = sport.nameAr,
            )
        }

        Text(
            text = if (selectedLang == "ar") sport.nameAr
            else if (selectedLang == "en") sport.nameEn
            else sport.nameFr,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.secondary
            } else Color.Black,
        )
    }
}