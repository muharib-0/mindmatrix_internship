package com.virasat.nammaguide.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.virasat.nammaguide.R
import com.virasat.nammaguide.ui.theme.*
import com.virasat.nammaguide.viewmodel.CheckInViewModel
import com.virasat.nammaguide.viewmodel.SiteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteDetailScreen(
    siteId: String,
    onBack: () -> Unit,
    siteVm: SiteViewModel = viewModel(),
    checkInVm: CheckInViewModel = viewModel()
) {
    // Load site on first composition
    LaunchedEffect(siteId) {
        siteVm.loadSite(siteId)
        siteVm.prepareAudio(0) // replace 0 with R.raw.audio_XXX when audio files exist
    }

    val site by siteVm.currentSite.collectAsStateWithLifecycle()
    val isKannada by siteVm.isKannada.collectAsStateWithLifecycle()
    val isPlaying by siteVm.isPlaying.collectAsStateWithLifecycle()
    val isCheckedIn by checkInVm.isCheckedIn(siteId)
        .collectAsStateWithLifecycle(initialValue = false)

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (site != null)
                            if (isKannada) site!!.nameKn else site!!.nameEn
                        else stringResource(R.string.title_site_detail),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TempleMaroon,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = AgedParchment
    ) { padding ->
        if (site == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TempleMaroon)
            }
            return@Scaffold
        }

        val s = site!!
        val bgColor = when (s.id) {
            "KA001" -> SiteColor1; "KA002" -> SiteColor2; "KA003" -> SiteColor3
            "KA004" -> SiteColor4; else -> SiteColor5
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero image placeholder ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = s.nameEn.first().toString(),
                    fontSize = 90.sp,
                    color = Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
            }

            // Gold separator
            Divider(color = SandstoneGold, thickness = 3.dp)

            Column(modifier = Modifier.padding(16.dp)) {

                // Site name
                Text(
                    text = if (isKannada) s.nameKn else s.nameEn,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TempleMaroon,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    color = SandstoneGold,
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                // ── Action buttons row ──────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Audio guide button
                    Button(
                        onClick = { siteVm.toggleAudio() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TempleMaroon
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Outlined.Pause
                                          else Icons.Outlined.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = if (isPlaying) stringResource(R.string.label_pause_audio)
                                   else stringResource(R.string.label_play_audio),
                            fontSize = 11.sp
                        )
                    }

                    // Language toggle button
                    OutlinedButton(
                        onClick = { siteVm.toggleLanguage() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp, SandstoneGold
                        )
                    ) {
                        Text(
                            text = if (isKannada) stringResource(R.string.label_switch_english)
                                   else stringResource(R.string.label_switch_kannada),
                            color = SandstoneGold,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Description ─────────────────────────────────────────────
                SectionHeader(stringResource(R.string.label_description))
                BodyText(if (isKannada) s.descriptionKn else s.descriptionEn)

                GoldDivider()

                // ── Architectural Note ───────────────────────────────────────
                SectionHeader(stringResource(R.string.label_architectural_note))
                BodyText(if (isKannada) s.architecturalNoteKn else s.architecturalNote)

                GoldDivider()

                // ── Local Legend (card) ──────────────────────────────────────
                SectionHeader(stringResource(R.string.label_local_legend))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = LegendCard),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SandstoneGold)
                ) {
                    Text(
                        text = if (isKannada) s.localLegendKn else s.localLegend,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmBrown,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(14.dp),
                        lineHeight = 22.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                // ── Hidden Fact ──────────────────────────────────────────────
                SectionHeader(stringResource(R.string.label_hidden_fact))
                Text(
                    text = if (isKannada) s.hiddenFactKn else s.hiddenFact,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MidBrown,
                    modifier = Modifier.padding(top = 6.dp, bottom = 24.dp),
                    lineHeight = 22.sp
                )

                // ── Check-In button ──────────────────────────────────────────
                Button(
                    onClick = {
                        checkInVm.checkIn(s)
                    },
                    enabled = !isCheckedIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SandstoneGold,
                        disabledContainerColor = Color(0xFFBBBBBB)
                    )
                ) {
                    Text(
                        text = if (isCheckedIn) stringResource(R.string.label_already_checked_in)
                               else stringResource(R.string.label_check_in),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isCheckedIn) Color.White else WarmBrown
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// ── Reusable sub-composables ─────────────────────────────────────────────────

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = TempleMaroon,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun BodyText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = WarmBrown,
        lineHeight = 24.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun GoldDivider() {
    Divider(
        color = SandstoneGold,
        thickness = 1.5.dp,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}
