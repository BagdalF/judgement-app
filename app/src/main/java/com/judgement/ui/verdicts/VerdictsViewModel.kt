package com.judgement.ui.verdicts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Verdicts
import com.judgement.data.repository.VerdictsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VerdictsUiState(
    val listaDeVerdicts: List<Verdicts> = emptyList(),
    val id_case: Int = 0,
    val id_user: Int = 0,
    val is_guilty: Boolean = false,
    val prison_years: String = "",
    val fine_amount: Int = 0,
    val verdictEmEdicao: Verdicts? = null
) {
    val textoBotao: String
        get() = if (verdictEmEdicao == null) "Adicionar Veredito" else "Atualizar Veredito"
}

class VerdictsViewModel(private val repository: VerdictsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(VerdictsUiState())
    val uiState: StateFlow<VerdictsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllVerdicts()?.collect { verdicts ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeVerdicts = verdicts)
                }
            }
        }
    }

    fun onCaseIdChange(newId: Int) {
        _uiState.update { it.copy(id_case = newId) }
    }

    fun onUserIdChange(newId: Int) {
        _uiState.update { it.copy(id_user = newId) }
    }

    fun onIsGuiltyChange(isGuilty: Boolean) {
        _uiState.update { it.copy(is_guilty = isGuilty) }
    }

    fun onPrisonYearsChange(years: String) {
        _uiState.update { it.copy(prison_years = years) }
    }

    fun onFineAmountChange(amount: Int) {
        _uiState.update { it.copy(fine_amount = amount) }
    }

    fun onEditar(verdict: Verdicts) {
        _uiState.update {
            it.copy(
                verdictEmEdicao = verdict,
                id_case = verdict.id_case,
                id_user = verdict.id_user,
                is_guilty = verdict.is_guilty,
                prison_years = verdict.prison_years,
                fine_amount = verdict.fine_amount
            )
        }
    }

    fun onDeletar(verdictId: Int) {
        viewModelScope.launch {
            repository.deleteVerdict(verdictId)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        val verdictParaSalvar = state.verdictEmEdicao?.copy(
            id_case = state.id_case,
            id_user = state.id_user,
            is_guilty = state.is_guilty,
            prison_years = state.prison_years,
            fine_amount = state.fine_amount
        ) ?: Verdicts(
            id_case = state.id_case,
            id_user = state.id_user,
            is_guilty = state.is_guilty,
            prison_years = state.prison_years,
            fine_amount = state.fine_amount
        )

        viewModelScope.launch {
            if (state.verdictEmEdicao == null) {
                repository.insertVerdict(verdictParaSalvar)
            } else {
                repository.updateVerdict(verdictParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                id_case = 0,
                id_user = 0,
                is_guilty = false,
                prison_years = "",
                fine_amount = 0,
                verdictEmEdicao = null
            )
        }
    }
}

class VerdictsViewModelFactory(private val repository: VerdictsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerdictsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VerdictsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
