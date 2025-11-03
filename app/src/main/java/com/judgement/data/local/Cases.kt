package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases")
data class Cases(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_defendant: Int,
    val id_plantiff: Int,
    val id_dialog: Int,
    val crime: String
)
