package com.github.adrijanrogan.etiketa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.composeunstyled.LocalContentColor
import com.composeunstyled.LocalTextStyle

@Immutable
data class Colors(
    val primary: Color,
    val onPrimary: Color,
    val contentPrimary: Color,
    val contentSecondary: Color,
)

@Immutable
data class Typography(
    val headingLarge: TextStyle,
    val headingMedium: TextStyle,
    val headingSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
)

val LocalColors = staticCompositionLocalOf {
    Colors(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        contentPrimary = Color.Unspecified,
        contentSecondary = Color.Unspecified,
    )
}

val LocalTypography = staticCompositionLocalOf {
    Typography(
        headingLarge = TextStyle.Default,
        headingMedium = TextStyle.Default,
        headingSmall = TextStyle.Default,
        bodyLarge = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        bodySmall = TextStyle.Default,
    )
}

object Theme {

    val ElementMaxWidth: Dp
        get() = 560.dp

    val colors: Colors
        @Composable
        get() = LocalColors.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Theme(
    content: @Composable () -> Unit
) {
    val primary = Color(0xFFFF5C00)
    val colors = Colors(
        primary = primary,
        onPrimary = Color.White,
        contentPrimary = Color(0xFF0A0C0C),
        contentSecondary = Color(0xFF1F2728),
    )

    /*val fontFamily = FontFamily(
        Font(
            resource = Res.font.Rubik,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Bold.weight),
            ),
            weight = FontWeight.Bold,
        ),
        Font(
            resource = Res.font.Rubik,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.SemiBold.weight),
            ),
            weight = FontWeight.SemiBold,
        ),
        Font(
            resource = Res.font.Rubik,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Medium.weight),
            ),
            weight = FontWeight.Medium,
        ),
        Font(
            resource = Res.font.Rubik,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Normal.weight),
            ),
            weight = FontWeight.Normal,
        ),
        Font(
            resource = Res.font.Rubik,
            variationSettings = FontVariation.Settings(
                FontVariation.weight(FontWeight.Light.weight),
            ),
            weight = FontWeight.Light,
        ),
    )*/

    val typography = Typography(
        headingLarge = TextStyle(
            fontSize = 28.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
        ),
        headingMedium = TextStyle(
            fontSize = 24.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
        ),
        headingSmall = TextStyle(
            fontSize = 17.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
        ),
        bodyLarge = TextStyle(
            fontSize = 18.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
        ),
        bodyMedium = TextStyle(
            fontSize = 16.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
        ),
        bodySmall = TextStyle(
            fontSize = 14.sp,
            lineHeight = 1.25.em,
            //fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
        ),
    )

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalTextStyle provides typography.bodyLarge,
        LocalContentColor provides colors.contentPrimary,
        content = content,
    )
}
