package com.kyant.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.ui.style.color.ColorSet
import com.kyant.ui.style.color.LocalColorSet
import com.kyant.ui.style.colorScheme
import com.kyant.ui.style.typography

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorSet: ColorSet = colorScheme.primary,
    disabledColorSet: ColorSet = ColorSet(
        colorScheme.surface.onColor.copy(alpha = 0.12f),
        colorScheme.surface.onColor.copy(alpha = 0.38f),
        colorScheme.surface.onColorVariant.copy(alpha = 0.38f)
    ),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val buttonColorSet = if (enabled) {
        colorSet
    } else {
        disabledColorSet
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        colorSet = buttonColorSet,
        border = border,
        interactionSource = interactionSource
    ) {
        ProvideTextStyle(value = typography.labelLarge) {
            CompositionLocalProvider(value = LocalIconSize provides 20.dp) {
                Row(
                    modifier = Modifier.padding(16.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun FilledTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorSet: ColorSet = colorScheme.primaryContainer,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorSet = colorSet,
    interactionSource = interactionSource,
    content = content
)

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = colorScheme.primary.color,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorSet = ColorSet(Color.Transparent, contentColor),
    disabledColorSet = ColorSet(
        Color.Transparent,
        colorScheme.surface.onColor.copy(alpha = 0.12f),
        colorScheme.surface.onColorVariant.copy(alpha = 0.12f)
    ),
    interactionSource = interactionSource,
    content = content
)

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LocalColorSet.current.onColor,
    border: BorderStroke? = BorderStroke(1.dp, color),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    colorSet = ColorSet(Color.Transparent, color),
    disabledColorSet = ColorSet(
        Color.Transparent,
        colorScheme.surface.onColor.copy(alpha = 0.12f),
        colorScheme.surface.onColorVariant.copy(alpha = 0.12f)
    ),
    border = border,
    interactionSource = interactionSource,
    content = content
)
