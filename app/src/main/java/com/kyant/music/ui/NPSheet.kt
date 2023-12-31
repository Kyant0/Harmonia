package com.kyant.music.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kyant.media.R
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.mediaStore
import com.kyant.music.ui.style.DynamicTheme
import com.kyant.music.ui.style.valueToken
import com.kyant.music.util.DeviceSpecs
import com.kyant.ui.BoxNoInline
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.navigation.OnBackPressed
import com.kyant.ui.softShadow
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun NPSheet(modifier: Modifier = Modifier) {
    with(NpSheetState) {
        val player = LocalPlayer.current
        val song = remember(player.currentMediaItem) {
            mediaStore.getSong(player.currentMediaItem?.mediaId)
        }
        DynamicTheme(song = song) {
            val scope = rememberCoroutineScope()
            val screenHeight = DeviceSpecs.size.height
            Surface(
                modifier = modifier
                    .fillMaxSize()
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            if (expandProgressValue > 0.01f) {
                                val y = lerp(
                                    constraints.maxHeight - 80.dp.toPx() - valueToken.safeBottomPadding.value.toPx(),
                                    0f,
                                    expandProgressValue
                                )
                                placeable.placeRelativeWithLayer(0, y.roundToInt()) {
                                    alpha = expandProgressValue * 10f
                                }
                            }
                        }
                    }
                    .draggable(
                        state = remember(screenHeight) {
                            draggableState(scope, screenHeight.toFloat())
                        },
                        orientation = Orientation.Vertical,
                        onDragStopped = { velocity ->
                            scope.launch {
                                fling(velocity, screenHeight.toFloat())
                            }
                        },
                        reverseDirection = true
                    ),
                colorSet = colorScheme.secondaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f, false)
                            .aspectRatio(1f)
                            .softShadow(
                                8.dp,
                                Rounding.Large.asSmoothRoundedShape(),
                                1f
                            )
                    ) {
                        BoxNoInline(propagateMinConstraints = true) {
                            val albumArt by produceState(initialValue = null as ImageBitmap?, song, collapsed) {
                                if (!collapsed) {
                                    value = withContext(Dispatchers.IO) {
                                        song?.coverArt?.asImageBitmap()
                                    }
                                }
                            }
                            albumArt?.let { bitmap ->
                                Image(
                                    bitmap = bitmap,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                SingleLineText(
                                    text = song?.title ?: "Unknown Title",
                                    style = typography.headlineSmall
                                )
                                SingleLineText(
                                    text = song?.displayArtist ?: "Unknown Artist",
                                    emphasis = 0.6f,
                                    style = typography.headlineSmall
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { player.skipToPrevious() },
                            size = 56.dp
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.skip_previous),
                                contentDescription = "Previous",
                                size = 48.dp
                            )
                        }
                        IconButton(
                            onClick = {
                                if (player.isPlaying) {
                                    player.pause()
                                } else {
                                    player.play()
                                }
                            },
                            size = 56.dp
                        ) {
                            if (player.isPlaying) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pause),
                                    contentDescription = "Pause",
                                    size = 48.dp
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.play),
                                    contentDescription = "Play",
                                    size = 48.dp
                                )
                            }
                        }
                        IconButton(
                            onClick = { player.skipToNext() },
                            size = 56.dp
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.skip_next),
                                contentDescription = "Next",
                                size = 48.dp
                            )
                        }
                    }
                }

                OnBackPressed(enabled = { targetExpandProgress > 0f }) {
                    scope.launch {
                        collapsePane()
                    }
                }
            }
        }
    }
}
