package com.judgement.data.repository

import android.util.Log
import com.judgement.data.local.Cases
import com.judgement.data.local.CasesDAO
import kotlinx.coroutines.flow.Flow

class CasesRepository(private val casesDao: CasesDAO) {
    fun getAllCases(): Flow<List<Cases>>? {
        return try {
            casesDao.getAll()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    fun getCaseById(id: Int): Flow<Cases>? {
        return try {
            casesDao.getById(id)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    fun getLastCase(): Flow<Cases>? {
        return try {
            casesDao.getLastCase()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun insertCase(case: Cases) {
        try {
            casesDao.insert(case)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    suspend fun deleteCase(id: Int) {
        try {
            casesDao.delete(id)
        } catch (e: Exception) {
            Log.e("Erro ao deletar", "${e.message}")
        }
    }

    suspend fun updateCase(case: Cases) {
        try {
            casesDao.update(case)
        } catch (e: Exception) {
            Log.e("Erro ao atualizar", "${e.message}")
        }
    }
}
