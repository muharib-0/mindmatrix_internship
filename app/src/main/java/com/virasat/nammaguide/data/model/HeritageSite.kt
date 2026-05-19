package com.virasat.nammaguide.data.model

/**
 * Data model representing a Karnataka heritage site.
 * All content is hardcoded (simulated GPS data — no real GPS needed).
 */
data class HeritageSite(
    val id: String,
    val nameEn: String,
    val nameKn: String,
    val latitude: Double,
    val longitude: Double,
    val taglineEn: String,
    val taglineKn: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val architecturalNote: String,
    val architecturalNoteKn: String,
    val localLegend: String,
    val localLegendKn: String,
    val hiddenFact: String,
    val hiddenFactKn: String,
    // Distance from the simulated user location (in km)
    val distanceKm: Double,
    // Drawable resource ID for the site thumbnail
    val thumbnailResId: Int = 0,
    // Raw resource ID for the audio guide (e.g., R.raw.audio_hampi)
    val audioResId: Int = 0
)
