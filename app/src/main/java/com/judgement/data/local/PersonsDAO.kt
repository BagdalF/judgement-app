package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonsDAO {
    @Insert
    suspend fun insert(person: Persons)

    @Query("SELECT * FROM persons")
    suspend fun getAll(): Flow<List<Persons>>

    @Query("SELECT * FROM persons WHERE id = :personId")
    suspend fun getById(personId: Int): Flow<Persons>

    @Query("DELETE FROM persons WHERE id = :personId")
    suspend fun delete(personId: Int)

    @Update
    suspend fun update(person: Persons)
}
