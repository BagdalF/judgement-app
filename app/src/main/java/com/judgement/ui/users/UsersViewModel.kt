package com.judgement.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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

    private fun onLoggedUserChange(newUser: Users) {
        _uiState.update { it.copy( loggedUser = newUser) }
    }

    fun onEditar(user : Users) {
        _uiState.update {
            it.copy(
                userEmEdicao = user,
                username = user.username,
                email = user.email,
                password = user.password,
                isAdmin = user.isAdmin
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

        if (state.username.isBlank() || state.email.isBlank()) {
            return
        }

        val userParaSalvar = state.userEmEdicao?.
        copy(
            username = state.username,
            email = state.email,
            password = state.password,
            isAdmin = state.isAdmin,
            firebaseId = FirebaseAuth.getInstance().currentUser?.uid
        ) ?: Users(
            username = state.username,
            email = state.email,
            password = state.password,
            isAdmin = state.isAdmin,
            firebaseId = FirebaseAuth.getInstance().currentUser?.uid
        )

        viewModelScope.launch {
            if (state.userEmEdicao == null) {
                repository.insertUser(userParaSalvar)
                onLoggedUserChange(userParaSalvar)
            } else {
                repository.updateUser(userParaSalvar)
                onLoggedUserChange(userParaSalvar)
            }
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