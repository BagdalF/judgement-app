package com.judgement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val firebaseId: String? = null,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean
)