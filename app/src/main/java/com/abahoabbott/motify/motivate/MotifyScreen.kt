package com.abahoabbott.motify.motivate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.notify.NotificationManagerHelper
import com.abahoabbott.motify.screens.home.ErrorScreen
import com.abahoabbott.motify.screens.home.LoadingScreen
import com.abahoabbott.motify.screens.home.MotifyUiState
import com.abahoabbott.motify.screens.home.QuoteOfTheDayCard
import com.abahoabbott.motify.ui.theme.MotifyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MotifyScreen(
    modifier: Modifier = Modifier,
    viewModel: MotifyViewModel = hiltViewModel()
) {

   val uiState =  viewModel.uiState.collectAsState().value

    when (uiState) {
        is MotifyUiState.Error -> ErrorScreen(
            errorMessage = uiState.message,
            modifier = modifier
        )

        MotifyUiState.Loading -> LoadingScreen(
            modifier = modifier
        )

        is MotifyUiState.Success -> MotifyScreenContents(
            modifier = modifier,
            currentQuote = uiState.quote,
            onSaveQuote ={viewModel.toggleFavorite(it)}

        )
    }


}


@Composable
private fun MotifyScreenContents(
    currentQuote: Quote,
    modifier: Modifier = Modifier,
    getNewQuote: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onSaveQuote:(quote:Quote)  -> Unit ={}
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val notificationManager = remember { NotificationManagerHelper(context) }
    val snackbarHostState  = remember { SnackbarHostState() }

// Permission state
    val hasNotificationPermission by remember {
        mutableStateOf(checkNotificationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Permission state is handled by checkNotificationPermission */ }
    )

    // Request notification permission if needed
    LaunchedEffect(Unit) {
        requestNotificationPermissionIfNeeded(hasNotificationPermission, permissionLauncher)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            QuoteOfTheDayCard(
                quote = currentQuote,
                onSaveClick = {
                    onSaveQuote(it)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (it.isSaved) "Removed from bookmarks" else "Saved to bookmarks"
                        )
                    }
                },
                onShareClick = {}
            )
            Spacer(modifier = Modifier.height(24.dp))


            ActionButtons(
                onNewQuote = {
                    handleNewQuoteRequest(
                        scope = scope,
                        updateVisibility = {},
                        getNextQuote = getNewQuote
                    )
                },
                onSendReminder = {
                    sendReminderNotification(
                        scope = scope,
                        notificationManager = notificationManager,
                        quote = currentQuote
                    )
                },
                isNotificationEnabled = hasNotificationPermission
            )

        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}


/**
 * Handles the request for a new quote, including animation logic.
 */
fun handleNewQuoteRequest(
    scope: kotlinx.coroutines.CoroutineScope,
    updateVisibility: (Boolean) -> Unit,
    getNextQuote: () -> Unit
) {
    // Hide current quote first
    updateVisibility(false)

    // Small delay before showing new quote
    scope.launch {
        delay(200)
        getNextQuote()
        updateVisibility(true)
    }
}

/**
 * Sends a notification with the current quote.
 */
private fun sendReminderNotification(
    scope: kotlinx.coroutines.CoroutineScope,
    notificationManager: NotificationManagerHelper,
    quote: Quote
) {
    scope.launch {
        notificationManager.showMotivationNotification(
            "Daily Motivation",
            quote
        )
    }
}

/**
 * Checks if notification permission is granted.
 *
 * @return true if permission is granted or not needed (pre-Tiramisu)
 */
private fun checkNotificationPermission(context: android.content.Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Permission not needed for older Android versions
    }
}

/**
 * Requests notification permission if needed and not already granted.
 */
private fun requestNotificationPermissionIfNeeded(
    hasPermission: Boolean,
    permissionLauncher: androidx.activity.result.ActivityResultLauncher<String>
) {
    if (!hasPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
@PreviewLightDark
private fun MotifyScreenContentsPreview() {
    MotifyTheme {
        Surface {
            MotifyScreenContents(
                currentQuote = Quote(
                    id="",
                    "Believe you can and you're halfway there.",
                    "Theodore Roosevelt"
                )
            )
        }
    }
}