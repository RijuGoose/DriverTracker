package com.riju.drivertracker.ui.triphistory.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riju.drivertracker.extensions.toLocalDateString
import com.riju.drivertracker.extensions.toTimeString
import com.riju.drivertracker.ui.theme.DriverTrackerTheme
import com.riju.drivertracker.ui.triphistory.model.TripHistoryItemUIModel
import java.time.ZoneId
import java.time.ZonedDateTime

private const val ROTATION_ANIMATION_DURATION = 3000
private const val ARROW_ANIMATION_DURATION = 2000
private const val ARROW_FADE_ANIMATION_DURATION = 1000

@Composable
fun TripHistoryItem(
    onTripSelected: (String) -> Unit,
    trip: TripHistoryItemUIModel,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Running Trip Animations")
    val angle by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
            infiniteRepeatable(
                animation = tween(ROTATION_ANIMATION_DURATION, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Running Trip Border Rotation"
        )

    val arrowPadding by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec =
            infiniteRepeatable(
                animation = tween(ARROW_ANIMATION_DURATION, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Running Trip Arrow Movement"
        )

    val arrowFade by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(ARROW_FADE_ANIMATION_DURATION, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Running Trip Arrow Fade"
    )

    val brush = Brush.sweepGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondary,
        )
    )

    Card(modifier = modifier.padding(horizontal = 8.dp)) {
        Card(
            modifier = Modifier
                .clipToBounds()
                .fillMaxWidth()
                .padding(2.dp)
                .clickable { onTripSelected(trip.tripId) }
                .drawWithContent {
                    if (trip.inProgress) {
                        rotate(angle) {
                            drawCircle(
                                brush = brush,
                                radius = size.width,
                                blendMode = BlendMode.SrcIn,
                            )
                        }
                    }
                    drawContent()
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.startTime.toLocalDateString(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = trip.startTime.toTimeString(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = trip.startLocation.orEmpty())
                }
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .then(
                            if (trip.inProgress && trip.endTime == null) {
                                Modifier
                                    .padding(start = arrowPadding.dp)
                                    .alpha(arrowFade)
                            } else {
                                Modifier
                            }
                        )
                )
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    trip.endTime?.let { endTime ->
                        Text(text = endTime.toLocalDateString(), fontWeight = FontWeight.Bold)
                        Text(text = endTime.toTimeString(), fontWeight = FontWeight.Bold)
                        Text(
                            text = trip.endLocation.orEmpty(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Suppress("MagicNumber")
@Preview(showBackground = true)
@Composable
fun TripHistoryItemPreview() {
    DriverTrackerTheme {
        TripHistoryItem(
            onTripSelected = {},
            trip = TripHistoryItemUIModel(
                tripId = "1",
                inProgress = true,
                startTime = ZonedDateTime.of(2021, 9, 2, 12, 0, 0, 0, ZoneId.of("UTC")),
                endTime = ZonedDateTime.of(2021, 9, 3, 13, 0, 0, 0, ZoneId.of("UTC")),
                startLocation = "Budapest",
                endLocation = null,
            )
        )
    }
}
