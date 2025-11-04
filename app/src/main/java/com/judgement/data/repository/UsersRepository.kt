package com.judgement.data.repository

import android.util.Log
import com.judgement.data.local.Users
import com.judgement.data.local.UsersDAO
import kotlinx.coroutines.flow.Flow

class UsersRepository (private val usersDao: UsersDAO) {
    suspend fun getAllUsers(): Flow<List<Users>>? {
        return try {
            usersDao.getAll()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun getUserById(id: Int): Flow<Users>? {
        return try {
            usersDao.getById(id)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun getUserByFirebaseId(fbId: String?): Flow<Users>? {
        return try {
            usersDao.getByFirebaseId(fbId)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun insertUser(
        user: Users
    ) {
        try {
            usersDao.insert(user)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    suspend fun deleteUser(id: Int) {
        try {
            usersDao.delete(id)
        } catch (e: Exception) {
            Log.e("Erro ao deletar", "${e.message}")
        }
    }

    suspend fun updateUser(
        user: Users
    ) {
        try {
            usersDao.update(
                user
            )
        } catch (e: Exception) {
            Log.e("Erro ao atualizar", "${e.message}")
        }
    }
}