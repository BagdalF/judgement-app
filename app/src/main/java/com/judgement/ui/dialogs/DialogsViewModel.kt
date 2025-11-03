package com.judgement.ui.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.judgement.data.local.Dialogs
import com.judgement.data.repository.DialogsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DialogsUiState(
    val listaDeDialogs: List<Dialogs> = emptyList(),
    val intro_plantiff: String = "",
    val intro_defendant: String = "",
    val question_defendant: String = "",
    val answer_defendant: String = "",
    val question_plantiff: String = "",
    val answer_plantiff: String = "",
    val dialogsEmEdicao: Dialogs? = null
) {
    val textoBotao: String
        get() = if (dialogsEmEdicao == null) "Adicionar Diálogo" else "Atualizar Diálogo"
}

class DialogsViewModel(private val repository: DialogsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DialogsUiState())
    val uiState: StateFlow<DialogsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllDialogs()?.collect { dialogs ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeDialogs = dialogs)
                }
            }
        }
    }

    fun onIntroPlantiffChange(newIntro: String) {
        _uiState.update { it.copy(intro_plantiff = newIntro) }
    }

    fun onIntroDefendantChange(newIntro: String) {
        _uiState.update { it.copy(intro_defendant = newIntro) }
    }

    fun onQuestionDefendantChange(newQuestion: String) {
        _uiState.update { it.copy(question_defendant = newQuestion) }
    }

    fun onAnswerDefendantChange(newAnswer: String) {
        _uiState.update { it.copy(answer_defendant = newAnswer) }
    }

    fun onQuestionPlantiffChange(newQuestion: String) {
        _uiState.update { it.copy(question_plantiff = newQuestion) }
    }

    fun onAnswerPlantiffChange(newAnswer: String) {
        _uiState.update { it.copy(answer_plantiff = newAnswer) }
    }

    fun onEditar(dialogs: Dialogs) {
        _uiState.update {
            it.copy(
                dialogsEmEdicao = dialogs,
                intro_plantiff = dialogs.intro_plantiff,
                intro_defendant = dialogs.intro_defendant,
                question_defendant = dialogs.question_defendant,
                answer_defendant = dialogs.answer_defendant,
                question_plantiff = dialogs.question_plantiff,
                answer_plantiff = dialogs.answer_plantiff
            )
        }
    }

    fun onDeletar(dialogsId: Int) {
        viewModelScope.launch {
            repository.deleteDialogs(dialogsId)
        }
    }

    fun onSalvar() {
        val state = _uiState.value

        val dialogsParaSalvar = state.dialogsEmEdicao?.copy(
            intro_plantiff = state.intro_plantiff,
            intro_defendant = state.intro_defendant,
            question_defendant = state.question_defendant,
            answer_defendant = state.answer_defendant,
            question_plantiff = state.question_plantiff,
            answer_plantiff = state.answer_plantiff
        ) ?: Dialogs(
            intro_plantiff = state.intro_plantiff,
            intro_defendant = state.intro_defendant,
            question_defendant = state.question_defendant,
            answer_defendant = state.answer_defendant,
            question_plantiff = state.question_plantiff,
            answer_plantiff = state.answer_plantiff
        )

        viewModelScope.launch {
            if (state.dialogsEmEdicao == null) {
                repository.insertDialogs(dialogsParaSalvar)
            } else {
                repository.updateDialogs(dialogsParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                intro_plantiff = "",
                intro_defendant = "",
                question_defendant = "",
                answer_defendant = "",
                question_plantiff = "",
                answer_plantiff = "",
                dialogsEmEdicao = null
            )
        }
    }
}

class DialogsViewModelFactory(private val repository: DialogsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DialogsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
