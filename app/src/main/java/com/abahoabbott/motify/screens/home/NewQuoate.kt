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
fun QuoteOfTheDayCardNew(
    quote: Quote,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
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
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.padding(24.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top-left quote icon
                Icon(
                    painter = painterResource(id = R.drawable.format_quote_24px),
                    contentDescription = "Opening quote icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(180f)
                )

                // Quote text
                Text(
                    text = quote.text,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = playfairDisplay,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        lineHeight = 28.sp
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                // Author
                Text(
                    text = quote.author.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = playfairDisplay
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                // Footer actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onSaveClick) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    IconButton(onClick = onShareClick) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Top-right label
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Today's quote",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Bottom-right quote icon above the share icon
            Icon(
                painter = painterResource(id = R.drawable.format_quote_24px),
                contentDescription = "Closing quote icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 56.dp) // Adjust distance above share icon
                    .size(32.dp)

            )
        }
    }
}

@Composable
@PreviewLightDark
private fun QuoteCardPreview() {
    MotifyTheme {
        Surface {
            QuoteOfTheDayCardNew(
                quote = Quote(
                    "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle.",
                    "Unknown"
                ),
                onSaveClick = {},
                onShareClick = {}
            )
        }
    }
}
