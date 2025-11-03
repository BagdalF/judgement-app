package com.judgement.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DialogsDAO {
    @Insert
    suspend fun insert(dialog: Dialogs)

    @Query("SELECT * FROM dialogs")
    suspend fun getAll(): Flow<List<Dialogs>>

    @Query("SELECT * FROM dialogs WHERE id = :dialogId")
    suspend fun getById(dialogId: Int): Flow<Dialogs>

    @Query("DELETE FROM dialogs WHERE id = :dialogId")
    suspend fun delete(dialogId: Int)

    @Update
    suspend fun update(dialog: Dialogs)
}
