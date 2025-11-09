package com.judgement.ui.persons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Persons
import com.judgement.data.repository.PersonsRepository
import com.judgement.services.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

data class PersonsUiState(
    val listaDePersons: List<Persons> = emptyList(),
)

class PersonsViewModel(private val repository: PersonsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonsUiState())
    val uiState: StateFlow<PersonsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchAPI()

            repository.getAllPersons()?.collect { persons ->
                _uiState.update { currentState ->
                    currentState.copy(listaDePersons = persons)
                }
            }
        }
    }

    fun fetchAPI() {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getPersons()

            for (responseAPIPerson in response) {
                val randomAge = (16..100).random()

                repository.insertPerson(
                    Persons(
                        name = responseAPIPerson.name,
                        address = "${responseAPIPerson.address.street}, ${responseAPIPerson.address.suite}, ${responseAPIPerson.address.city}",
                        phone = responseAPIPerson.phone,
                        email = responseAPIPerson.email,
                        age = randomAge
                    )
                )
            }
        }
    }

    fun onDeletar(personId: Int) {
        viewModelScope.launch {
            repository.deletePerson(personId)
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
