package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CasesDAO {
    @Insert
    suspend fun insert(case: Cases)

    @Query("SELECT * FROM cases")
    fun getAll(): Flow<List<Cases>>

    @Query("SELECT * FROM cases ORDER BY id DESC LIMIT 1")
    fun getLastCase(): Flow<Cases>

    @Query("SELECT * FROM cases WHERE id = :caseId")
    fun getById(caseId: Int): Flow<Cases>

    @Query("DELETE FROM cases WHERE id = :caseId")
    suspend fun delete(caseId: Int)

    @Update
    suspend fun update(case: Cases)
}
