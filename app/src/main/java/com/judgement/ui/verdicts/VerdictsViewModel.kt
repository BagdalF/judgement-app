package com.judgement.ui.verdicts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.currentUser
import com.judgement.data.local.*
import com.judgement.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class VerdictWithCase(
    val verdict: Verdicts,
    val case: Cases? = null,
    val defendant: Persons? = null,
    val plaintiff: Persons? = null
)

data class VerdictsUiState(
    val verdictsWithCases: List<VerdictWithCase> = emptyList(),
    val idCase: Int = 0,
    val idUser: Int = 0,
    val isGuilty: Boolean = false,
    val prisonYears: String = "",
    val fineAmount: Int = 0,
    val verdictEmEdicao: Verdicts? = null
)

class VerdictsViewModel(
    private val verdictsRepository: VerdictsRepository,
    private val casesRepository: CasesRepository,
    personsRepository: PersonsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(VerdictsUiState())
    val uiState: StateFlow<VerdictsUiState> = _uiState.asStateFlow()

    private val casesFlow = casesRepository.getAllCases() ?: emptyFlow()
    private val personsFlow = personsRepository.getAllPersons() ?: emptyFlow()
    private val verdictsFlow = verdictsRepository.getAllVerdicts() ?: emptyFlow()

    init {
        viewModelScope.launch {
            if (casesFlow.first().isNotEmpty() && !verdictsFlow.first().isNotEmpty()) {
                val firstCase = casesFlow.first()[0]
                val verdict = Verdicts(
                    idCase = firstCase.id,
                    idUser = currentUser?.id ?: 1,
                    isGuilty = true,
                    prisonYears = "5",
                    fineAmount = 10000
                )
                verdictsRepository.insertVerdict(verdict)
            }

            loadVerdicts()
        }
    }

    fun loadVerdicts() {
        viewModelScope.launch {
            combine(
                verdictsFlow,
                casesFlow,
                personsFlow
            ) { verdicts, cases, persons ->
                verdicts.map { verdict ->
                    val case = cases.find { it.id == verdict.idCase }
                    val defendant = case?.let { c -> persons.find { it.id == c.idDefendant } }
                    val plaintiff = case?.let { c -> persons.find { it.id == c.idPlaintiff } }
                    VerdictWithCase(verdict, case, defendant, plaintiff)
                }
            }.collect { verdictsWithCases ->
                _uiState.update {
                    it.copy(verdictsWithCases =
                        verdictsWithCases.filter { verdict ->
                            currentUser?.isAdmin == true || verdict.verdict.idUser == (currentUser?.id ?: 1)
                        }
                    )
                }
            }
        }
    }

    fun onIsGuiltyChange(isGuilty: Boolean) {
        _uiState.update { it.copy(isGuilty = isGuilty) }
    }

    fun onPrisonYearsChange(years: String) {
        _uiState.update { it.copy(prisonYears = years) }
    }

    fun onFineAmountChange(amount: Int) {
        _uiState.update { it.copy(fineAmount = amount) }
    }

    fun onDeletar(verdictId: Int, caseId: Int) {
        viewModelScope.launch {
            verdictsRepository.deleteVerdict(verdictId)
            casesRepository.deleteCase(caseId)
        }
    }

    fun onSalvar(idCase: Int, idUser: Int, isGuilty: Boolean, prisonYears: String, fineAmount: Int) {
        viewModelScope.launch {
            val verdictParaSalvar = Verdicts(
            idCase = idCase,
            idUser = idUser,
            isGuilty = isGuilty,
            prisonYears = prisonYears,
            fineAmount = fineAmount
        )
            verdictsRepository.insertVerdict(verdictParaSalvar)
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                idCase = 0,
                idUser = 0,
                isGuilty = false,
                prisonYears = "",
                fineAmount = 0,
                verdictEmEdicao = null
            )
        }
    }
}

class VerdictsViewModelFactory(
    private val verdictsRepository: VerdictsRepository,
    private val casesRepository: CasesRepository,
    private val personsRepository: PersonsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerdictsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VerdictsViewModel(verdictsRepository, casesRepository, personsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
