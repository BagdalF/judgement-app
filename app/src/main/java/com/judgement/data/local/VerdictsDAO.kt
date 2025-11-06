package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VerdictsDAO {
    @Insert
    suspend fun insert(verdict: Verdicts)

    @Query("SELECT * FROM verdicts")
    fun getAll(): Flow<List<Verdicts>>

    @Query("SELECT * FROM verdicts WHERE id = :verdictId")
    fun getById(verdictId: Int): Flow<Verdicts>

    @Query("DELETE FROM verdicts WHERE id = :verdictId")
    suspend fun delete(verdictId: Int)

    @Update
    suspend fun update(verdict: Verdicts)
}
