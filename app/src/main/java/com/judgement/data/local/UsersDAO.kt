package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    @Insert
    suspend fun insert(user: Users)

    @Query("SELECT * FROM users")
    suspend fun getAll(): Flow<List<Users>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getById(userId: Int): Flow<Users>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun delete(userId: Int)

    @Update
    suspend fun update(user: Users)
}