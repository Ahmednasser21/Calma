package com.metafortech.calma.presentation.authentication.sport

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.authentication.CircularProgressOnLoadingState
import com.metafortech.calma.presentation.authentication.NextButton
import com.metafortech.calma.presentation.authentication.StringError

@Composable
fun SportSelectionScreen(
    modifier: Modifier = Modifier,
    state: SportSelectionUiState,
    selectSport: (String) -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        Modifier.rotate(180f)
                    )
                }
            }

            Text(
                text = stringResource(R.string.select_sport_type_you_prefer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
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
                        val isSelected = sport.id == state.selectedSportId
                        SportItem(
                            sport = sport,
                            isSelected = isSelected,
                            onClick = {
                                selectSport(sport.id)
                            }
                        )
                    }
                }
            }

            CircularProgressOnLoadingState(state.isLoading)

            StringError(state.error,modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .weight(1f))

            NextButton (enabled = state.selectedSportId != null ){  onNextClick()}

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SportItem(
    sport: Sport,
    isSelected: Boolean,
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
            Image(
                painter = painterResource(id = sport.imageResId),
                contentDescription = stringResource(id = sport.nameResId),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = stringResource(id = sport.nameResId),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) {
                MaterialTheme.colorScheme.secondary
            } else Color.Black,
        )
    }
}