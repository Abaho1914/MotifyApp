package com.abahoabbott.motify.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abahoabbott.motify.R
import com.abahoabbott.motify.data.Quote
import com.abahoabbott.motify.ui.theme.MotifyTheme

/**
 * Displays a stylized card showing a "Quote of the Day" with decorative quote icons,
 * author attribution, and actionable buttons for saving or sharing the quote.
 *
 * @param quote The [Quote] object containing the text and author of the quote.
 * @param onSaveClick Callback triggered when the save (bookmark) icon is clicked.
 * @param onShareClick Callback triggered when the share icon is clicked.
 * @param modifier Optional [Modifier] for customizing the appearance and layout of the card.
 *
 *
 *  */

@Composable
fun QuoteOfTheDayCard(
    quote: Quote,
    modifier: Modifier = Modifier
) {
    val playfairDisplay = FontFamily(Font(R.font.playfair_display))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics { contentDescription = "Quote of the day" },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Quote text
                Text(
                    text ="\" ${quote.text} \"",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = playfairDisplay,
                        fontStyle = FontStyle.Normal,
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        lineHeight = 32.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                // Author
                Text(
                    text = "- ${quote.author}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = playfairDisplay
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun QuoteCardPreview() {
    MotifyTheme {
        Surface {
            QuoteOfTheDayCard(
                quote = Quote(
                    id = "",
                    "The only way to do great work is to love what you do. If you haven\'t found it yet, keep looking. Don\'t settle.",
                    "Steve Jobs"
                )
            )
        }
    }
}
