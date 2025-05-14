package com.abahoabbott.motify.motivate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.abahoabbott.motify.ui.theme.MotifyTheme

/**
 * Displays a motivational quote with a fade-in animation.
 *
 * @param quote The motivational quote to display
 * @param isVisible Controls the visibility of the quote
 * @param modifier Optional modifier for the card
 */
@Composable
fun QuoteCard(
    quote: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500))
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = quote,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/** Row of buttons for user actions.
 *
 * @param onNewQuote Callback when user requests a new quote
 * @param onSendReminder Callback when user wants to set a reminder
 * @param isNotificationEnabled Whether notification permission is granted
 * @param modifier Optional modifier for the button row
 **/
@Composable
fun ActionButtons(
    onNewQuote: () -> Unit,
    onSendReminder: () -> Unit,
    isNotificationEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Next quote button
        Button(
            onClick = onNewQuote,
            modifier = Modifier.weight(1f)
        ) {
            Text("New Quote")
        }

        // Send notification button
        OutlinedButton(
            onClick = onSendReminder,
            modifier = Modifier.weight(1f),
            enabled = isNotificationEnabled
        ) {
            Text("Remind Me")
        }
    }
}

/**
 * Displays a prompt to request notification permission.
 *
 * @param onRequestPermission Callback to trigger permission request
 * @param modifier Optional modifier for the prompt
 */
@Composable
fun NotificationPermissionPrompt(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        ),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            "Notification permission is required for reminders",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
        TextButton(
            onClick = onRequestPermission,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Grant Permission")
        }
    }
}


@Composable
@PreviewLightDark
private fun QuoteCardPreview() {
    MotifyTheme {
        Surface {
            QuoteCard(
                quote = "Dread it,Run from it, Destiny arrives all the same",
                isVisible = true
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun ActionButtonsPreview() {
    MotifyTheme {
        Surface {
            ActionButtons(
                onNewQuote = {},
                onSendReminder = {},
                isNotificationEnabled = true
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun NotificationPermissionPromptPreview() {
    MotifyTheme {
        Surface {
            NotificationPermissionPrompt(
                onRequestPermission = {}
            )
        }
    }
}