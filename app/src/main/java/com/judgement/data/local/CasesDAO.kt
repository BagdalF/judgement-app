package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CasesDAO {
    @Insert
    suspend fun insert(case: Cases)

    @Query("SELECT * FROM cases")
    suspend fun getAll(): Flow<List<Cases>>

    @Query("SELECT * FROM cases WHERE id = :caseId")
    suspend fun getById(caseId: Int): Flow<Cases>

    @Query("DELETE FROM cases WHERE id = :caseId")
    suspend fun delete(caseId: Int)

    @Update
    suspend fun update(case: Cases)
}
