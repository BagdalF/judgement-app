package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dialogs")
data class Dialogs(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val introPlaintiff: String,
    val introDefendant: String,
    val questionDefendant: String,
    val answerDefendant: String,
    val questionPlaintiff: String,
    val answerPlaintiff: String
)
