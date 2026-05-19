package com.virasat.nammaguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.virasat.nammaguide.R
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.ui.theme.*
import com.virasat.nammaguide.viewmodel.SiteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDiscoveryScreen(
    onSiteClick: (String) -> Unit,
    vm: SiteViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AgedParchment)
    ) {
        // ── Header ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TempleMaroon)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = stringResource(R.string.label_discover_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.label_discover_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = SandstoneGoldLight
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Gold lotus divider
            Divider(color = SandstoneGold, thickness = 2.dp)
        }

        // ── Site Grid ───────────────────────────────────────────────────────
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(vm.allSites) { site ->
                SiteCard(site = site, onClick = { onSiteClick(site.id) })
            }
        }
    }
}

@Composable
private fun SiteCard(site: HeritageSite, onClick: () -> Unit) {
    val bgColor = when (site.id) {
        "KA001" -> SiteColor1
        "KA002" -> SiteColor2
        "KA003" -> SiteColor3
        "KA004" -> SiteColor4
        else    -> SiteColor5
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WarmOffWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TempleMaroon)
    ) {
        Column {
            // Thumbnail placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                // Large initial letter
                Text(
                    text = site.nameEn.first().toString(),
                    fontSize = 48.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                )
                // Distance badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(TempleMaroon.copy(alpha = 0.85f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = String.format("%.1f km", site.distanceKm),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Gold separator
            Divider(color = SandstoneGold, thickness = 2.dp)

            // Site info
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = site.nameEn,
                    style = MaterialTheme.typography.labelLarge,
                    color = TempleMaroon,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = site.taglineEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MidBrown,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
