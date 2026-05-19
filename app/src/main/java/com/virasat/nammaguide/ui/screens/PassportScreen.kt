package com.virasat.nammaguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.virasat.nammaguide.R
import com.virasat.nammaguide.data.db.CheckInEntity
import com.virasat.nammaguide.ui.theme.*
import com.virasat.nammaguide.viewmodel.CheckInViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassportScreen(vm: CheckInViewModel = viewModel()) {
    val checkIns by vm.allCheckIns.collectAsStateWithLifecycle()

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
                text = stringResource(R.string.label_passport_title),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))

            // Badge count row
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circular badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SandstoneGold),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = checkIns.size.toString(),
                        color = WarmBrown,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "${checkIns.size} ${stringResource(R.string.label_stamps_collected)}",
                    color = SandstoneGoldLight,
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = SandstoneGold, thickness = 2.dp)
        }

        // ── Content ────────────────────────────────────────────────────────
        if (checkIns.isEmpty()) {
            EmptyPassport()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(checkIns) { index, checkIn ->
                    PassportStampCard(checkIn = checkIn, stampNumber = index + 1)
                }
            }
        }
    }
}

@Composable
private fun EmptyPassport() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🏛", fontSize = 72.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.label_passport_empty_title),
            style = MaterialTheme.typography.titleLarge,
            color = TempleMaroon,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.label_passport_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MidBrown,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun PassportStampCard(checkIn: CheckInEntity, stampNumber: Int) {
    val formatter = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WarmOffWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SandstoneGold)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stamp badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(TempleMaroon)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🏛", fontSize = 22.sp)
                    Text(
                        text = "#$stampNumber",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            // Site info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = checkIn.siteName,
                    style = MaterialTheme.typography.titleMedium,
                    color = TempleMaroon,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${stringResource(R.string.label_visited_on)} ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MidBrown
                    )
                    Text(
                        text = formatter.format(Date(checkIn.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = SandstoneGold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Gold star
            Text("⭐", fontSize = 22.sp)
        }
    }
}
