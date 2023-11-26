package com.kyant.music.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.music.service.LocalPlayer
import com.kyant.music.storage.MediaStore
import com.kyant.music.ui.theme.isDark
import com.kyant.music.util.AsyncImage
import com.kyant.ui.Icon
import com.kyant.ui.IconButton
import com.kyant.ui.SingleLineText
import com.kyant.ui.Surface
import com.kyant.ui.Text
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.shape.Rounding
import com.kyant.ui.style.typography
import com.kyant.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun LibraryNavigator.Songs() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SingleLineText(
                text = "Songs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .graphicsLayer {
                        translationX = (paneExpandProgressValue - targetPaneExpandProgress) * width / 2f
                    },
                color = colorScheme.primary.color,
                emphasis = lerp(
                    0.8f,
                    0.2f,
                    (paneExpandProgressValue - targetPaneExpandProgress).absoluteValue * 2f
                ),
                textAlign = TextAlign.Center,
                style = typography.headlineLarge
            )
        }

        val player = LocalPlayer.current
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(Rounding.Large.asSmoothRoundedShape()),
            state = state,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(MediaStore.songs, { song -> song.mediaId }) { song ->
                Surface(
                    onClick = { player.playFromMediaId(song.mediaId) },
                    shape = Rounding.ExtraSmall.asRoundedShape(),
                    colorSet = colorScheme.surfaceContainerLowest
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp, 8.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = song.thumbnailUri,
                                modifier = Modifier.clip(Rounding.Small.asSmoothRoundedShape())
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = song.title,
                                style = typography.bodyLarge
                            )
                            Text(
                                text = song.displayArtist,
                                emphasis = if (isDark) 0.6f else 0.5f,
                                style = typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                emphasis = 0.5f
                            )
                        }
                    }
                }
            }
        }
    }
}
