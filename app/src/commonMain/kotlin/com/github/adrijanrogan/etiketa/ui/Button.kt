package com.github.adrijanrogan.etiketa.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.UnstyledButton

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(50),
    content: @Composable (RowScope.() -> Unit)
) {
    UnstyledButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        backgroundColor = Theme.colors.primary,
        contentColor = Theme.colors.onPrimary,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides Theme.typography.bodyLarge.merge(
                fontWeight = FontWeight.Medium,
            ),
        ) {
            content()
        }
    }
}
