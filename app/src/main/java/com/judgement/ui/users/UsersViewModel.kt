package com.judgement.ui.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.currentUser
import com.judgement.data.local.Users
import com.judgement.data.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UsersUiState(
    val listaDeUsers: List<Users> = emptyList(),
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val isAdmin: Boolean = false,
    val userEmEdicao: Users? = null
)


class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())

    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val usersDatabase = repository.getAllUsers()

            usersDatabase?.collect{
                users ->

                if (users.isEmpty()) {
                    repository.insertUser(
                        Users(
                            id = 1,
                            firebaseId = "qYQ8UPs5N3bwN7UigQoAAPjomOg1",
                            username = "felipe",
                            email = "felipe@gmail.com",
                            password = "felipe",
                            isAdmin = true
                        )
                    )
                }

                _uiState.update {
                        currentState ->
                    currentState.copy(listaDeUsers = users)
                }
            }
        }
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.update { it.copy( username = newUsername ) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy( email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy( password = newPassword) }
    }

    fun onIsAdminToggle() {
        _uiState.update { it.copy( isAdmin = !it.isAdmin ) }
    }

    fun onEditar(user : Users) {
        _uiState.update {
            it.copy(
                userEmEdicao = user,
                username = _uiState.value.username,
                email = _uiState.value.email,
                password = _uiState.value.password,
                isAdmin = user.isAdmin
            )
        }
    }

    fun onDeletar(idUser : Int) {
        viewModelScope.launch {
            repository.deleteUser(idUser)
        }
    }

    fun onSalvar(firebaseId: String) {
        val state = _uiState.value

        if (state.username.isBlank() || state.email.isBlank()) {
            return
        }

        val userParaSalvar = state.userEmEdicao?.
        copy(
            username = state.username,
            email = state.email,
            password = state.password,
            isAdmin = state.isAdmin,
            firebaseId = firebaseId
        ) ?: Users(
            username = state.username,
            email = state.email,
            password = state.password,
            isAdmin = state.isAdmin,
            firebaseId = firebaseId
        )

        viewModelScope.launch {
            if (state.userEmEdicao == null) {
                repository.insertUser(userParaSalvar)
            } else {
                repository.updateUser(userParaSalvar)
            }

            currentUser = userParaSalvar
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                username = "",
                email = "",
                password = "",
                userEmEdicao = null
            )
        }
    }

    fun onGetCurrentUser(sessionId: String) {
        viewModelScope.launch {
            try {
                val user = _uiState.value.listaDeUsers.find { it.firebaseId == sessionId }
                if (user != null) {
                    currentUser = user
                } else {
                    Log.e("UsersViewModel", "User not found with Firebase ID: $sessionId")
                    throw Exception("User profile not found")
                }
            } catch (e: Exception) {
                Log.e("UsersViewModel", "Error getting user: ${e.message}")
                throw e
            }
        }
    }
}

class UsersViewModelFactory(private val repository: UsersRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}