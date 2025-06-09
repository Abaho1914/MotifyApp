package com.abahoabbott.motify.motivate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abahoabbott.motify.R
import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.screens.home.ErrorScreen
import com.abahoabbott.motify.screens.home.LoadingScreen
import com.abahoabbott.motify.screens.home.MotifyUiState
import com.abahoabbott.motify.screens.home.QuoteOfTheDayCard
import com.abahoabbott.motify.ui.theme.MotifyTheme


@Composable
fun MotifyScreen(
    modifier: Modifier = Modifier,
    viewModel: MotifyViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value


    MotifyScreenComponents(
        modifier,
        uiState,
        sendReminderNotification = {
            viewModel.sendReminderNotification(it)
        },
        toggleFavorite = {
            viewModel.toggleFavorite(it)
        }
    )
}

@Composable
fun MotifyScreenComponents(
    modifier: Modifier = Modifier,
    uiState: MotifyUiState,
    sendReminderNotification: (Quote) -> Unit = {},
    toggleFavorite: (Quote) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {


    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            MotifyTopAppBar()
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(start = 8.dp, end = 8.dp, top = 8.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when (uiState) {
                is MotifyUiState.Error -> ErrorScreen(
                    errorMessage = uiState.message,
                    modifier = Modifier.fillMaxSize()
                )

                MotifyUiState.Loading -> LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )

                is MotifyUiState.Success -> {
                    QuoteOfTheDayCard(
                        quote = uiState.quote,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 32.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HomeTextButton(
                            onClick = { sendReminderNotification(it) },
                            uiState = uiState,
                            text = "Share"
                        )

                        HomeTextButton(
                            text = "Save",
                            uiState = uiState,
                            onClick = { toggleFavorite(it) }
                        )
//                        HomeScreenButton(
//                            icon = if (uiState.quote.isSaved) R.drawable.baseline_bookmark_24
//                            else R.drawable.baseline_bookmark_border_24,
//                            onClick = {
//                                toggleFavorite(it)
//                            },
//                            uiState = uiState
//                        )
                    }
                }
            }
            LaunchedEffect(uiState) {
                if (uiState is MotifyUiState.Success && uiState.quote.isSaved) {
                    val quote = uiState.quote
                    if (quote.isSaved) {
                        snackbarHostState.showSnackbar("Saved to bookmarks")
                    }
                }
            }
        }
    }
}

@Composable
private fun MotifyTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Quote of the Day",
            style = MaterialTheme.typography.titleLarge,
         //   fontSize = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,

        )
        IconButton(onClick = { /* Handle settings click */ }) {
            Icon(
                painter = painterResource(R.drawable.settings_outlined_24px),
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun HomeTextButton(
    onClick: (Quote) -> Unit,
    text: String,
    uiState: MotifyUiState.Success
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = { onClick(uiState.quote) }
    ) {
        Text(text,
            fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HomeScreenButton(
    icon: Int,
    onClick: (Quote) -> Unit,
    uiState: MotifyUiState.Success
) {
    IconButton(
        onClick = { onClick(uiState.quote) },
    ) {

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .size(32.dp),
            painter = painterResource(icon),
            contentDescription = "Localized description",
            tint = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
@PreviewLightDark
private fun MotifyScreenPreview() {
    MotifyTheme {

        val quote = Quote(
            id = "",
            "Believe you can and you're halfway there.",
            "Theodore Roosevelt"
        )

        val uiState = MotifyUiState.Success(
            quote

        )
        MotifyScreenComponents(
            uiState = uiState
        )
    }
}

@Composable
@PreviewLightDark
private fun MotifyScreenLoading() {

    MotifyTheme {
        Surface {
            MotifyScreenComponents(
                uiState = MotifyUiState.Loading

            )
        }
    }

}

@Composable
@PreviewLightDark
private fun MotifyScreenError() {

    MotifyTheme {
        Surface {
            MotifyScreenComponents(
                uiState = MotifyUiState.Error("No quote of the day")
            )
        }
    }

}