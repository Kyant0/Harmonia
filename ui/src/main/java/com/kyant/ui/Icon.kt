/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kyant.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.ui.theme.LocalColorToken
import com.kyant.ui.theme.LocalEmphasis
import com.kyant.ui.theme.applyEmphasis
import com.kyant.ui.util.thenIfNotNull

/**
 * A Material Design icon component that draws [imageVector] using [tint], with a default value
 * of [LocalContentColor]. If [imageVector] has no intrinsic size, this component will use the
 * recommended default size. Icon is an opinionated component designed to be used with single-color
 * icons so that they can be tinted correctly for the component they are placed in. For multicolored
 * icons and icons that should not be tinted, use [Color.Unspecified] for [tint]. For generic images
 * that should not be tinted, and do not follow the recommended icon size, use the generic
 * [androidx.compose.foundation.Image] instead. For a clickable icon, see [IconButton].
 *
 * To learn more about icons, see [Material Design icons](https://m3.material.io/styles/icons/overview)
 *
 * @param imageVector [ImageVector] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint tint to be applied to [imageVector]. If [Color.Unspecified] is provided, then no tint
 * is applied.
 */
@Composable
fun Icon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = LocalIconSize.current,
    tint: Color = LocalColorToken.current.contentColor,
    emphasis: Float = LocalEmphasis.current.iconEmphasis
) {
    Icon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        tint = tint,
        emphasis = emphasis
    )
}

/**
 * A Material Design icon component that draws [bitmap] using [tint], with a default value
 * of [LocalContentColor]. If [bitmap] has no intrinsic size, this component will use the
 * recommended default size. Icon is an opinionated component designed to be used with single-color
 * icons so that they can be tinted correctly for the component they are placed in. For multicolored
 * icons and icons that should not be tinted, use [Color.Unspecified] for [tint]. For generic images
 * that should not be tinted, and do not follow the recommended icon size, use the generic
 * [androidx.compose.foundation.Image] instead. For a clickable icon, see [IconButton].
 *
 * To learn more about icons, see [Material Design icons](https://m3.material.io/styles/icons/overview)
 *
 * @param bitmap [ImageBitmap] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint tint to be applied to [bitmap]. If [Color.Unspecified] is provided, then no tint is
 * applied.
 */
@Composable
fun Icon(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = LocalIconSize.current,
    tint: Color = LocalColorToken.current.contentColor,
    emphasis: Float = LocalEmphasis.current.iconEmphasis
) {
    val painter = remember(bitmap) { BitmapPainter(bitmap) }
    Icon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        tint = tint,
        emphasis = emphasis
    )
}

/**
 * A Material Design icon component that draws [painter] using [tint], with a default value
 * of [LocalContentColor]. If [painter] has no intrinsic size, this component will use the
 * recommended default size. Icon is an opinionated component designed to be used with single-color
 * icons so that they can be tinted correctly for the component they are placed in. For multicolored
 * icons and icons that should not be tinted, use [Color.Unspecified] for [tint]. For generic images
 * that should not be tinted, and do not follow the recommended icon size, use the generic
 * [androidx.compose.foundation.Image] instead. For a clickable icon, see [IconButton].
 *
 * To learn more about icons, see [Material Design icons](https://m3.material.io/styles/icons/overview)
 *
 * @param painter [Painter] to draw inside this icon
 * @param contentDescription text used by accessibility services to describe what this icon
 * represents. This should always be provided unless this icon is used for decorative purposes, and
 * does not represent a meaningful action that a user can take. This text should be localized, such
 * as by using [androidx.compose.ui.res.stringResource] or similar
 * @param modifier the [Modifier] to be applied to this icon
 * @param tint tint to be applied to [painter]. If [Color.Unspecified] is provided, then no tint is
 * applied.
 */
@Composable
fun Icon(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = LocalIconSize.current,
    tint: Color = LocalColorToken.current.contentColor,
    emphasis: Float = LocalEmphasis.current.iconEmphasis
) {
    val colorFilter = if (tint.isUnspecified) null else ColorFilter.tint(tint.applyEmphasis(emphasis))
    Box(
        modifier
            .toolingGraphicsLayer()
            .size(size)
            .paint(painter, colorFilter = colorFilter, contentScale = ContentScale.Fit)
            .thenIfNotNull(contentDescription) {
                semantics {
                    this.contentDescription = it
                    this.role = Role.Image
                }
            }
    )
}

// Default icon size, for icons with no intrinsic size information
val LocalIconSize = staticCompositionLocalOf { 24.dp }