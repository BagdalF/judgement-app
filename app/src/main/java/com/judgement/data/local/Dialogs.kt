package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialogs")
data class Dialogs(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val intro_plantiff: String,
    val intro_defendant: String,
    val question_defendant: String,
    val answer_defendant: String,
    val question_plantiff: String,
    val answer_plantiff: String
)
