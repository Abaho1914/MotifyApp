package com.abahoabbott.motify.motivate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
fun HomeActionsRow(
    onNavigateToSaved: () -> Unit,
    onNavigateToGoals: () -> Unit,
    onNavigateToReminders: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        ActionButton(icon = Icons.Filled.Favorite, label = "Saved", onClick = onNavigateToSaved)
        ActionButton(icon = Icons.Filled.CheckCircle, label = "Goals", onClick = onNavigateToGoals)
        ActionButton(icon = Icons.Filled.Notifications, label = "Reminders", onClick = onNavigateToReminders)
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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