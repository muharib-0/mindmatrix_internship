package com.virasat.nammaguide.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a user's check-in at a heritage site.
 */
@Entity(tableName = "check_ins")
data class CheckInEntity(
    @PrimaryKey val siteId: String,
    val siteName: String,
    val timestamp: Long,
    val isCheckedIn: Boolean
)
