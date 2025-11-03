package com.judgement.data.repository

import android.util.Log
import com.judgement.data.local.Persons
import com.judgement.data.local.PersonsDAO
import kotlinx.coroutines.flow.Flow

class PersonsRepository(private val personsDao: PersonsDAO) {
    suspend fun getAllPersons(): Flow<List<Persons>>? {
        return try {
            personsDao.getAll()
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun getPersonById(id: Int): Flow<Persons>? {
        return try {
            personsDao.getById(id)
        } catch (e: Exception) {
            Log.e("Erro ao buscar", "${e.message}")
            null
        }
    }

    suspend fun insertPerson(person: Persons) {
        try {
            personsDao.insert(person)
        } catch (e: Exception) {
            Log.e("Erro ao adicionar", "Msg: ${e.message}")
        }
    }

    suspend fun deletePerson(id: Int) {
        try {
            personsDao.delete(id)
        } catch (e: Exception) {
            Log.e("Erro ao deletar", "${e.message}")
        }
    }

    suspend fun updatePerson(person: Persons) {
        try {
            personsDao.update(person)
        } catch (e: Exception) {
            Log.e("Erro ao atualizar", "${e.message}")
        }
    }
}
