package com.virasat.nammaguide.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.virasat.nammaguide.data.db.CheckInEntity
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.data.repository.SiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CheckInViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteRepository(application)

    /** All check-ins as a hot StateFlow — powers PassportScreen */
    val allCheckIns: StateFlow<List<CheckInEntity>> =
        repository.getAllCheckIns()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    /** Per-site check-in status as a cold Flow — collected in SiteDetailScreen */
    fun isCheckedIn(siteId: String): Flow<Boolean> =
        repository.isCheckedIn(siteId)

    fun checkIn(site: HeritageSite) {
        viewModelScope.launch {
            repository.checkIn(site)
        }
    }
}
