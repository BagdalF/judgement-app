package com.judgement.ui.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUiState(
    var sessionId: String? = null
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun register(email: String, senha: String, nome: String, userViewModel: UsersViewModel){
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, senha).await()

                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build()

                auth.currentUser?.updateProfile(profileUpdate)?.await()

                _uiState.value.sessionId = auth.currentUser?.uid

                userViewModel.onSalvar(auth.currentUser!!.uid)

            }catch (e: Exception){
                Log.e("AuthViewModel", "Erro no cadastro: ${e.message}")
            }
        }
    }

    fun login(email: String, senha: String, userViewModel: UsersViewModel) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, senha).await()
                val firebaseUser = result.user ?: throw Exception("Authentication failed")
                
                _uiState.value = _uiState.value.copy(sessionId = firebaseUser.uid)
                
                userViewModel.onGetCurrentUser(firebaseUser.uid)
             } catch (e: TimeoutCancellationException) {
                _uiState.value = _uiState.value.copy(sessionId = null)
                throw Exception("Um erro aconteceu, tente novamente", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(sessionId = null)
                throw e
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _uiState.value.sessionId = null
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