package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firebaseId: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String
)