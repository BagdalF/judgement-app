package com.judgement.ui.cases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Cases
import com.judgement.data.repository.CasesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CasesUiState(
    val listaDeCases: List<Cases> = emptyList(),
    val id_defendant: Int = 0,
    val id_plantiff: Int = 0,
    val id_dialog: Int = 0,
    val crime: String = "",
    val caseEmEdicao: Cases? = null
) {
    val textoBotao: String
        get() = if (caseEmEdicao == null) "Adicionar Caso" else "Atualizar Caso"
}

class CasesViewModel(private val repository: CasesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CasesUiState())
    val uiState: StateFlow<CasesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCases()?.collect { cases ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeCases = cases)
                }
            }
        }
    }

    fun onDefendantChange(newId: Int) {
        _uiState.update { it.copy(id_defendant = newId) }
    }

    fun onPlantiffChange(newId: Int) {
        _uiState.update { it.copy(id_plantiff = newId) }
    }

    fun onDialogChange(newId: Int) {
        _uiState.update { it.copy(id_dialog = newId) }
    }

    fun onCrimeChange(newCrime: String) {
        _uiState.update { it.copy(crime = newCrime) }
    }

    fun onEditar(case: Cases) {
        _uiState.update {
            it.copy(
                caseEmEdicao = case,
                id_defendant = case.id_defendant,
                id_plantiff = case.id_plantiff,
                id_dialog = case.id_dialog,
                crime = case.crime
            )
        }
    }

    fun onDeletar(idCase: Int) {
        viewModelScope.launch {
            repository.deleteCase(idCase)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        if (state.crime.isBlank()) {
            return
        }

        val caseParaSalvar = state.caseEmEdicao?.copy(
            id_defendant = state.id_defendant,
            id_plantiff = state.id_plantiff,
            id_dialog = state.id_dialog,
            crime = state.crime
        ) ?: Cases(
            id_defendant = state.id_defendant,
            id_plantiff = state.id_plantiff,
            id_dialog = state.id_dialog,
            crime = state.crime
        )

        viewModelScope.launch {
            if (state.caseEmEdicao == null) {
                repository.insertCase(caseParaSalvar)
            } else {
                repository.updateCase(caseParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                id_defendant = 0,
                id_plantiff = 0,
                id_dialog = 0,
                crime = "",
                caseEmEdicao = null
            )
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
