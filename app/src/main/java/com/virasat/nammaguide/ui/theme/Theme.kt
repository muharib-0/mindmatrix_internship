package com.virasat.nammaguide.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VirasatColorScheme = lightColorScheme(
    primary            = TempleMaroon,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFFFDAD6),
    onPrimaryContainer = TempleMaroonDark,
    secondary          = SandstoneGold,
    onSecondary        = WarmBrown,
    secondaryContainer = Color(0xFFFFE0A0),
    onSecondaryContainer = WarmBrown,
    background         = AgedParchment,
    onBackground       = WarmBrown,
    surface            = WarmOffWhite,
    onSurface          = WarmBrown,
    surfaceVariant     = LegendCard,
    onSurfaceVariant   = MidBrown,
    outline            = SandstoneGold,
    outlineVariant     = Color(0xFFD4A96A),
    error              = Color(0xFFBA1A1A),
    onError            = Color.White,
)

@Composable
fun VirasatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VirasatColorScheme,
        typography  = VirasatTypography,
        content     = content
    )
}
