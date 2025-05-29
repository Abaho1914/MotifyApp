package com.abahoabbott.motify.motivate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.abahoabbott.motify.ui.theme.MotifyTheme


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
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("New Quote")
        }

        // Send notification button
        OutlinedButton(
            onClick = onSendReminder,
            modifier = Modifier.weight(1f),
            enabled = isNotificationEnabled,
            shape = MaterialTheme.shapes.medium
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
    modifier: Modifier = Modifier,
    onRequestPermission: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    Card(
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                alpha = 0.5f
            ),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Stay on Track with Reminders",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "To send you motivational quotes, Motify needs permission to show notifications.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (onDismiss != null) Arrangement.End else Arrangement.Center // Center if only one button
            ) {
                if (onDismiss != null) {
                    TextButton(onClick = onDismiss) {
                        Text("Maybe Later")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Button( // Use a filled Button for the primary action for more emphasis
                    onClick = onRequestPermission,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Turn On Notifications")
                }
            }
        }
    }
}

@Composable
fun MoodSelector(
    selectedMood: String?,
    onMoodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val moods = listOf("😊 Happy", "😐 Okay", "😢 Sad", "😡 Angry", "😴 Tired")

    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(moods) { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { onMoodSelected(mood) },
                    label = { Text(mood) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}


@Composable
@PreviewLightDark
private fun MoodsSelectorPreview() {
    MotifyTheme {
        Surface {
            MoodSelector(
                selectedMood = "😊 Happy",
                onMoodSelected = {}
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