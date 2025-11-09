package com.judgement.ui.cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Cases
import com.judgement.data.local.Persons
import com.judgement.data.repository.CasesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CasesUiState(
    val listaDeCases: List<Cases> = emptyList(),
    val caseEmEdicao: Cases? = null,
    val currentCase: Cases? = null,
    val currentDefendant: Persons? = null,
    val currentPlaintiff: Persons? = null
)

class CasesViewModel(private val repository: CasesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CasesUiState())
    val uiState: StateFlow<CasesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            createRandomCase(10)
            val casesDatabase = repository.getAllCases()

            casesDatabase?.collect{
                cases ->

                _uiState.update {
                        currentState ->
                    currentState.copy(listaDeCases = cases)
                }
            }
        }
    }

    fun onDeletar(idCase: Int) {
        viewModelScope.launch {
            repository.deleteCase(idCase)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        viewModelScope.launch {
            repository.insertCase(state.currentCase!!)

            repository.getLastCase()?.collect { lastCase ->
                _uiState.update { currentState ->
                    currentState.copy(
                        currentCase = lastCase
                    )
                }
            }
        }
    }

    fun generateNewCase(availablePersons: List<Persons>) {
        val newCase = Cases(
            idDefendant = (1..availablePersons.size).random(),
            idPlaintiff = (1..availablePersons.size).random(),
            idDialog = (1..10).random(),
            crime = RANDOM_CRIMES.random()
        )

        _uiState.update { currentState ->
            currentState.copy(
                currentCase = newCase,
                currentDefendant = availablePersons.find { it.id == newCase.idDefendant },
                currentPlaintiff = availablePersons.find { it.id == newCase.idPlaintiff }
            )
        }
    }

    fun clearCurrentCase() {
        _uiState.update { it.copy(
            currentCase = null,
            currentDefendant = null,
            currentPlaintiff = null
        ) }
    }

    companion object {
        val RANDOM_CRIMES = listOf(
            "Homicídio",
            "Roubo",
            "Agressão",
            "Fraude",
            "Vandalismo",
            "Assalto",
            "Invasão de Propriedade",
            "Falsificação",
            "Extorsão",
            "Conspiração"
        )
    }

    fun createRandomCase(availablePersons: Int) {
        viewModelScope.launch {
            val newCase = Cases(
                idDefendant = (1..availablePersons).random(),
                idPlaintiff = (1..availablePersons).random(),
                idDialog = (1..availablePersons).random(),
                crime = RANDOM_CRIMES.random()
            )

            repository.insertCase(newCase)
        }
    }
}

class CasesViewModelFactory(private val repository: CasesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CasesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CasesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
