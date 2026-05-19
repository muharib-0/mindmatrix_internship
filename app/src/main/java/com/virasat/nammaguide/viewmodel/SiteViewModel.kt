package com.virasat.nammaguide.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.data.repository.SiteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SiteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteRepository(application)

    // ── All sites for Discovery screen ────────────────────────────────────────
    val allSites: List<HeritageSite> = repository.allSites

    // ── Currently loaded site ─────────────────────────────────────────────────
    private val _currentSite = MutableStateFlow<HeritageSite?>(null)
    val currentSite: StateFlow<HeritageSite?> = _currentSite.asStateFlow()

    fun loadSite(siteId: String) {
        _currentSite.value = repository.getSiteById(siteId)
    }

    // ── Language toggle (false = English, true = Kannada) ────────────────────
    private val _isKannada = MutableStateFlow(false)
    val isKannada: StateFlow<Boolean> = _isKannada.asStateFlow()

    fun toggleLanguage() {
        _isKannada.value = !_isKannada.value
    }

    // ── Audio guide (MediaPlayer) ─────────────────────────────────────────────
    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun prepareAudio(audioResId: Int) {
        releaseMediaPlayer()
        if (audioResId == 0) return
        try {
            mediaPlayer = MediaPlayer.create(getApplication(), audioResId)?.apply {
                setOnCompletionListener { _isPlaying.value = false }
            }
        } catch (e: Exception) {
            Log.e("SiteViewModel", "MediaPlayer error: ${e.message}")
        }
    }

    fun toggleAudio() {
        val mp = mediaPlayer ?: return
        if (mp.isPlaying) {
            mp.pause()
            _isPlaying.value = false
        } else {
            mp.start()
            _isPlaying.value = true
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.apply { if (isPlaying) stop(); release() }
        mediaPlayer = null
        _isPlaying.value = false
    }

    /** CRITICAL: release MediaPlayer here to prevent crash on back-navigation */
    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }
}
