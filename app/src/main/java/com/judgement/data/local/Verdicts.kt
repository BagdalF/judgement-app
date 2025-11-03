package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verdicts")
data class Verdicts(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_case: Int,
    val id_user: Int,
    val is_guilty: Boolean,
    val prison_years: String,
    val fine_amount: Int
)
