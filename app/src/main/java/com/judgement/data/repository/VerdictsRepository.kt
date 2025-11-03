package com.judgement.data.repository

import android.util.Log
import com.judgement.data.local.Verdicts
import com.judgement.data.local.VerdictsDAO
import kotlinx.coroutines.flow.Flow

class VerdictsRepository(private val verdictsDao: VerdictsDAO) {
    suspend fun getAllVerdicts(): Flow<List<Verdicts>>? {
        return try {
            verdictsDao.getAll()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun getVerdictById(id: Int): Flow<Verdicts>? {
        return try {
            verdictsDao.getById(id)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun insertVerdict(verdict: Verdicts) {
        try {
            verdictsDao.insert(verdict)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    suspend fun deleteVerdict(id: Int) {
        try {
            verdictsDao.delete(id)
        } catch (e: Exception) {
            Log.e("Erro ao deletar", "${e.message}")
        }
    }

    suspend fun updateVerdict(verdict: Verdicts) {
        try {
            verdictsDao.update(verdict)
        } catch (e: Exception) {
            Log.e("Erro ao atualizar", "${e.message}")
        }
    }
}
