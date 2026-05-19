package com.virasat.nammaguide.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for check-in operations.
 * Uses Flow for reactive UI updates and coroutines for writes.
 */
@Dao
interface CheckInDao {

    /**
     * Insert or replace a check-in record.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkIn: CheckInEntity)

    /**
     * Observe all check-ins reactively.
     */
    @Query("SELECT * FROM check_ins WHERE isCheckedIn = 1 ORDER BY timestamp DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntity>>

    /**
     * Observe whether the user has checked in to a specific site.
     */
    @Query("SELECT isCheckedIn FROM check_ins WHERE siteId = :siteId LIMIT 1")
    fun isCheckedIn(siteId: String): Flow<Boolean>
}
