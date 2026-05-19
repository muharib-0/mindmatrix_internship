package com.virasat.nammaguide.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.data.repository.SiteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for PassportFragment and the Check-In action in SiteDetailFragment.
 *
 * Manages:
 * - Persisting check-ins to Room DB
 * - Observing all check-ins (reactively via Flow → LiveData)
 * - Checking whether a specific site is already checked in
 */
class CheckInViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteRepository(application)

    /** Reactive list of all check-ins for PassportFragment */
    val allCheckIns = repository.getAllCheckIns().asLiveData()

    /** Observe check-in status for a given site */
    fun isCheckedIn(siteId: String) = repository.isCheckedIn(siteId).asLiveData()

    /** Perform check-in on IO coroutine */
    fun checkIn(site: HeritageSite) {
        viewModelScope.launch {
            repository.checkIn(site)
        }
    }
}
