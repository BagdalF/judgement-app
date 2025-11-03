package com.judgement.ui.persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Persons
import com.judgement.data.repository.PersonsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PersonsUiState(
    val listaDePessoas: List<Persons> = emptyList(),
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val age: Int = 0,
    val pessoaEmEdicao: Persons? = null
) {
    val textoBotao: String
        get() = if (pessoaEmEdicao == null) "Adicionar Pessoa" else "Atualizar Pessoa"
}

class PersonsViewModel(private val repository: PersonsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonsUiState())
    val uiState: StateFlow<PersonsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPersons()?.collect { persons ->
                _uiState.update { currentState ->
                    currentState.copy(listaDePessoas = persons)
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onAddressChange(newAddress: String) {
        _uiState.update { it.copy(address = newAddress) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phone = newPhone) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onAgeChange(newAge: Int) {
        _uiState.update { it.copy(age = newAge) }
    }

    fun onEditar(person: Persons) {
        _uiState.update {
            it.copy(
                pessoaEmEdicao = person,
                name = person.name,
                address = person.address,
                phone = person.phone,
                email = person.email,
                age = person.age
            )
        }
    }

    fun onDeletar(personId: Int) {
        viewModelScope.launch {
            repository.deletePerson(personId)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        if (state.name.isBlank() || state.email.isBlank()) {
            return
        }

        val personParaSalvar = state.pessoaEmEdicao?.copy(
            name = state.name,
            address = state.address,
            phone = state.phone,
            email = state.email,
            age = state.age
        ) ?: Persons(
            name = state.name,
            address = state.address,
            phone = state.phone,
            email = state.email,
            age = state.age
        )

        viewModelScope.launch {
            if (state.pessoaEmEdicao == null) {
                repository.insertPerson(personParaSalvar)
            } else {
                repository.updatePerson(personParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                name = "",
                address = "",
                phone = "",
                email = "",
                age = 0,
                pessoaEmEdicao = null
            )
        }
    }
}

class PersonsViewModelFactory(private val repository: PersonsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
