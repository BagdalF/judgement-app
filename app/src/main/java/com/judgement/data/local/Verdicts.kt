package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verdicts")
data class Verdicts(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idCase: Int,
    val idUser: Int,
    val isGuilty: Boolean,
    val prisonYears: String,
    val fineAmount: Int
)
