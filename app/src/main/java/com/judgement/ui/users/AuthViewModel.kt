package com.judgement.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

data class AuthUiState(
    var sessionId: String? = null
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

//    private val _userState = MutableStateFlow(auth.currentUser)
//    private val userState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // feedback UI
    private val _authFeedback = MutableStateFlow<String?>(null)
    val authFeedback: StateFlow<String?> = _authFeedback

    // loading
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun signUp(email: String, senha: String, nome: String){
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null

            try {
                auth.createUserWithEmailAndPassword(email, senha).await()

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()

                auth.currentUser?.updateProfile(profileUpdate)?.await()

                _uiState.value.sessionId = auth.currentUser?.uid
                _authFeedback.value = "Cadastro realizado com sucesso! :) "

            }catch (e: Exception){
                _authFeedback.value = e.message ?: "Erro no cadastro :/ "
            }finally {
                _loading.value = false
            }
        }
    }

    fun signIn(email: String, senha: String, userState: UsersViewModel) {
        viewModelScope.launch {
            _loading.value = true
            _authFeedback.value = null

            try {
                auth.signInWithEmailAndPassword(email, senha).await()
//                val user = userState.onGetCurrentUser(auth.currentUser?.uid!!)
                _uiState.value.sessionId = auth.currentUser?.uid
            }catch (e: Exception){
                _authFeedback.value = e.message ?: "Erro no login :/ "
            }finally {
                _loading.value = false
            }
        }
    }

    //    fun onLogin(): Users? {
//        val state = _uiState.value
//
//        val user = state.listaDeUsers.find { it.email == state.email && it.password == state.password }
//
//        return user
//    }

    fun updateUser(){

    }

    fun signOut(userState: UsersViewModel){
        auth.signOut()
        _uiState.value.sessionId = null
    }

    fun clearFeedback(){
        _authFeedback.value = null
    }

}

class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}