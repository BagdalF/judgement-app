package com.judgement.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Users
import com.judgement.data.repository.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UsersUiState(
    val listaDeUsers: List<Users> = emptyList(),
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val userEmEdicao: Users? = null,
    val loggedUser: Users? = null
) {
    val textoBotao: String
        get() = if (userEmEdicao == null) "Adicionar User" else "Atualizar User"
}


class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())

    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllUsers()?.collect{
                    users ->
                _uiState.update {
                        currentState ->
                    currentState.copy(listaDeUsers = users)
                }
            }
        }
    }

    fun onFirstNameChange(newFirstName: String) {
        _uiState.update { it.copy( firstName = newFirstName) }
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.update { it.copy( lastName = newLastName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy( email = newEmail) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy( phone = newPhone) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy( password = newPassword) }
    }

    fun onEditar(user : Users) {
        _uiState.update {
            it.copy(
                userEmEdicao = user,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phone = user.phone,
                password = user.password
            )
        }
    }

    fun onDeletar(idUser : Int) {
        viewModelScope.launch {
            repository.deleteUser(idUser)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        if (state.firstName.isBlank() || state.lastName.isBlank() || state.email.isBlank() || state.phone.isBlank()) {
            return
        }

        val userParaSalvar = state.userEmEdicao?.
        copy(
            firstName = state.firstName,
            lastName = state.lastName,
            email = state.email,
            phone = state.phone,
            password = state.password
        ) ?: Users(
            firstName = state.firstName,
            lastName = state.lastName,
            email = state.email,
            phone = state.phone,
            password = state.password
        )

        viewModelScope.launch {
            if (state.userEmEdicao == null) {
                repository.insertUser(userParaSalvar)
            } else {
                repository.updateUser(userParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                firstName = "",
                lastName = "",
                email = "",
                phone = "",
                password = "",
                userEmEdicao = null
            )
        }
    }

//    fun onLogin(): Users? {
//        val state = _uiState.value
//
//        val user = state.listaDeUsers.find { it.email == state.email && it.password == state.password }
//
//        return user
//    }

    fun onGetCurrentUser(sessionId: String): Users? {
        var user: Users? = null

        viewModelScope.launch {
            repository.getUserByFirebaseId(sessionId)?.collect{
                fbUser ->
                user = fbUser
                _uiState.update { currentState ->
                    currentState.copy(loggedUser = user)
                }
            }
        }

        return user
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