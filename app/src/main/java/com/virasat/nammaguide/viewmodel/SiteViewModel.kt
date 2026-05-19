package com.virasat.nammaguide.viewmodel

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.data.repository.SiteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for SiteDetailFragment.
 *
 * Manages:
 * - Currently selected site data
 * - MediaPlayer lifecycle (plays/pauses audio guide)
 * - Language toggle state (EN/KN)
 *
 * MediaPlayer is released in onCleared() to prevent crashes on back-navigation.
 */
class SiteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteRepository(application)

    // ─── Currently displayed site ────────────────────────────────────────────

    private val _currentSite = MutableLiveData<HeritageSite?>()
    val currentSite: LiveData<HeritageSite?> get() = _currentSite

    fun loadSite(siteId: String) {
        _currentSite.value = repository.getSiteById(siteId)
    }

    // ─── All sites for Discovery screen ──────────────────────────────────────

    val allSites: List<HeritageSite> = repository.allSites

    // ─── Language toggle ─────────────────────────────────────────────────────

    private val _isKannada = MutableLiveData(false)
    val isKannada: LiveData<Boolean> get() = _isKannada

    fun toggleLanguage() {
        _isKannada.value = !(_isKannada.value ?: false)
    }

    // ─── MediaPlayer ─────────────────────────────────────────────────────────

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    /**
     * Initialise MediaPlayer with the given raw resource.
     * Called each time a new site is loaded.
     */
    fun prepareAudio(audioResId: Int) {
        releaseMediaPlayer()
        if (audioResId == 0) return
        try {
            mediaPlayer = MediaPlayer.create(getApplication(), audioResId)?.apply {
                setOnCompletionListener {
                    _isPlaying.postValue(false)
                }
            }
        } catch (e: Exception) {
            Log.e("SiteViewModel", "MediaPlayer prepare error: ${e.message}")
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
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
        _isPlaying.value = false
    }

    /**
     * CRITICAL: Release MediaPlayer here to prevent resource leak and crash
     * when user navigates back.
     */
    override fun onCleared() {
        super.onCleared()
        releaseMediaPlayer()
    }
}
