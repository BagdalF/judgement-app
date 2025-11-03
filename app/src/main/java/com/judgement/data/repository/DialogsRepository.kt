package com.judgement.data.repository

import android.util.Log
import com.judgement.data.local.Dialogs
import com.judgement.data.local.DialogsDAO
import kotlinx.coroutines.flow.Flow

class DialogsRepository(private val dialogsDao: DialogsDAO) {
    suspend fun getAllDialogs(): Flow<List<Dialogs>>? {
        return try {
            dialogsDao.getAll()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun getDialogsById(id: Int): Flow<Dialogs>? {
        return try {
            dialogsDao.getById(id)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun insertDialogs(dialogs: Dialogs) {
        try {
            dialogsDao.insert(dialogs)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    suspend fun deleteDialogs(id: Int) {
        try {
            dialogsDao.delete(id)
        } catch (e: Exception) {
            Log.e("Erro ao deletar", "${e.message}")
        }
    }

    suspend fun updateDialogs(dialogs: Dialogs) {
        try {
            dialogsDao.update(dialogs)
        } catch (e: Exception) {
            Log.e("Erro ao atualizar", "${e.message}")
        }
    }
}
