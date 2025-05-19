package com.abahoabbott.motify.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.motivate.HomeActionsRow
import com.abahoabbott.motify.motivate.MoodSelector
import com.abahoabbott.motify.ui.theme.MotifyTheme

@Composable
fun MotifyHomeScreen(
    quote: Quote,
    selectedMood: String?,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onMoodSelected: (String) -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onNavigateToReminders: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuoteOfTheDayCardNew(
            quote = quote,
            onSaveClick = onSaveClick,
            onShareClick = onShareClick
        )

        MoodSelector(
            selectedMood = selectedMood,
            onMoodSelected = onMoodSelected
        )

        HomeActionsRow(
            onNavigateToSaved = onNavigateToSaved,
            onNavigateToGoals = onNavigateToGoals,
            onNavigateToReminders = onNavigateToReminders
        )
    }
}

@PreviewLightDark
@Composable
fun MotifyHomeScreenPreview() {
    val sampleQuote = Quote(
        text = "The only way to do great work is to love what you do.",
        author = "Steve Jobs",
        isSaved = false
    )

    var selectedMood by remember { mutableStateOf<String?>(null) }

    MotifyTheme {
        Surface {
            MotifyHomeScreen(
                quote = sampleQuote,
                selectedMood = selectedMood,
                onSaveClick = {},
                onShareClick = {},
                onMoodSelected = { mood -> selectedMood = mood },
                onNavigateToSaved = {},
                onNavigateToGoals = {},
                onNavigateToReminders = {}
            )
        }
    }
}
