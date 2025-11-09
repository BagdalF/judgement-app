package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases")
data class Cases(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idDefendant: Int,
    val idPlaintiff: Int,
    val idDialog: Int,
    val crime: String
)
