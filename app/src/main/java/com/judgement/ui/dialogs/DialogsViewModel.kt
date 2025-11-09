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
    val introPlaintiff: String = "",
    val introDefendant: String = "",
    val questionDefendant: String = "",
    val answerDefendant: String = "",
    val questionPlaintiff: String = "",
    val answerPlaintiff: String = "",
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
            val dialogsDatabase = repository.getAllDialogs()

            dialogsDatabase?.collect{
                dialogs ->

                if (dialogs.isEmpty()) {
                    repository.insertDialog(
                        Dialogs(
                            id = 1,
                            introPlaintiff = "Excelência, venho aqui hoje para apresentar um caso de danos materiais causados pelo réu.",
                            introDefendant = "Nego todas as acusações, Meritíssimo. Os danos alegados não foram causados por mim.",
                            questionDefendant = "Por que você estava no local no momento do incidente?",
                            answerDefendant = "Eu estava apenas passando pelo local a caminho do trabalho, como faço todos os dias.",
                            questionPlaintiff = "Pode explicar como identificou o réu como responsável pelos danos?",
                            answerPlaintiff = "Tenho câmeras de segurança que registraram todo o incidente."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 2,
                            introPlaintiff = "Meritíssimo, estou processando por quebra de contrato comercial.",
                            introDefendant = "As acusações são infundadas. O contrato foi cumprido conforme acordado.",
                            questionDefendant = "Onde está a documentação que comprova suas entregas no prazo?",
                            answerDefendant = "Todos os comprovantes de entrega estão aqui, assinados pelo representante do autor.",
                            questionPlaintiff = "Como explica os atrasos documentados nas entregas?",
                            answerPlaintiff = "Tenho um registro detalhado de todas as entregas atrasadas."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 3,
                            introPlaintiff = "Solicito indenização por assédio moral no ambiente de trabalho.",
                            introDefendant = "As alegações são falsas. Sempre mantive um ambiente profissional.",
                            questionDefendant = "Possui alguma prova das supostas condutas inadequadas?",
                            answerDefendant = "Não há nenhuma evidência porque isso nunca aconteceu.",
                            questionPlaintiff = "Pode descrever os episódios de assédio?",
                            answerPlaintiff = "Tenho e-mails e testemunhas que confirmam os eventos."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 4,
                            introPlaintiff = "Processo por violação de direitos autorais de minha obra.",
                            introDefendant = "A obra em questão é significativamente diferente e original.",
                            questionDefendant = "Como desenvolveu sua versão da obra?",
                            answerDefendant = "Foi um trabalho totalmente original, desenvolvido ao longo de meses.",
                            questionPlaintiff = "Quando descobriu a cópia de sua obra?",
                            answerPlaintiff = "Ao ver o produto deles no mercado, três meses atrás."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 5,
                            introPlaintiff = "Busco ressarcimento por serviços não prestados adequadamente.",
                            introDefendant = "Os serviços foram executados conforme o contrato.",
                            questionDefendant = "Qual foi o escopo acordado inicialmente?",
                            answerDefendant = "Exatamente o que foi entregue, conforme documento assinado.",
                            questionPlaintiff = "Por que considera o serviço inadequado?",
                            answerPlaintiff = "O resultado final não corresponde às especificações contratadas."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 6,
                            introPlaintiff = "Processo por danos morais devido a difamação nas redes sociais.",
                            introDefendant = "Apenas expressei minha opinião baseada em fatos.",
                            questionDefendant = "Como justifica as acusações públicas feitas?",
                            answerDefendant = "Tudo que publiquei está respaldado por evidências.",
                            questionPlaintiff = "Qual o impacto dessas publicações em sua vida?",
                            answerPlaintiff = "Perdi oportunidades de trabalho e sofri ataques online."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 7,
                            introPlaintiff = "Solicito pagamento de pensão alimentícia em atraso.",
                            introDefendant = "Estou impossibilitado de pagar devido a dificuldades financeiras.",
                            questionDefendant = "Qual sua atual situação financeira?",
                            answerDefendant = "Perdi meu emprego e tenho outras dívidas essenciais.",
                            questionPlaintiff = "Como tem mantido as despesas das crianças?",
                            answerPlaintiff = "Tenho feito empréstimos para cobrir as necessidades básicas."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 8,
                            introPlaintiff = "Processo por acidente de trânsito com danos graves.",
                            introDefendant = "O acidente foi inevitável devido às condições da via.",
                            questionDefendant = "Qual era sua velocidade no momento do impacto?",
                            answerDefendant = "Estava dentro do limite permitido, a 60 km/h.",
                            questionPlaintiff = "Há testemunhas do acidente?",
                            answerPlaintiff = "Sim, três pessoas presenciaram e já prestaram depoimento."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 9,
                            introPlaintiff = "Peço anulação de contrato por vício no produto.",
                            introDefendant = "O produto foi entregue em perfeitas condições.",
                            questionDefendant = "Realizou a manutenção recomendada?",
                            answerDefendant = "O problema surgiu por uso inadequado do cliente.",
                            questionPlaintiff = "Quando notou o defeito?",
                            answerPlaintiff = "Logo no primeiro uso, documentei com fotos e vídeos."
                        )
                    )

                    repository.insertDialog(
                        Dialogs(
                            id = 10,
                            introPlaintiff = "Ação de despejo por inadimplência.",
                            introDefendant = "Solicito prazo adicional para regularização.",
                            questionDefendant = "Quantos meses está em atraso?",
                            answerDefendant = "São três meses, mas já tenho parte do valor.",
                            questionPlaintiff = "Houve tentativa de acordo anterior?",
                            answerPlaintiff = "Sim, duas vezes, mas os pagamentos não foram cumpridos."
                        )
                    )
                }

                _uiState.update {
                        currentState ->
                    currentState.copy(listaDeDialogs = dialogs)
                }
            }
        }
    }

    fun onIntroPlaintiffChange(newIntro: String) {
        _uiState.update { it.copy(introPlaintiff = newIntro) }
    }

    fun onIntroDefendantChange(newIntro: String) {
        _uiState.update { it.copy(introDefendant = newIntro) }
    }

    fun onQuestionDefendantChange(newQuestion: String) {
        _uiState.update { it.copy(questionDefendant = newQuestion) }
    }

    fun onAnswerDefendantChange(newAnswer: String) {
        _uiState.update { it.copy(answerDefendant = newAnswer) }
    }

    fun onQuestionPlaintiffChange(newQuestion: String) {
        _uiState.update { it.copy(questionPlaintiff = newQuestion) }
    }

    fun onAnswerPlaintiffChange(newAnswer: String) {
        _uiState.update { it.copy(answerPlaintiff = newAnswer) }
    }

    fun onEditar(dialogs: Dialogs) {
        _uiState.update {
            it.copy(
                dialogsEmEdicao = dialogs,
                introPlaintiff = dialogs.introPlaintiff,
                introDefendant = dialogs.introDefendant,
                questionDefendant = dialogs.questionDefendant,
                answerDefendant = dialogs.answerDefendant,
                questionPlaintiff = dialogs.questionPlaintiff,
                answerPlaintiff = dialogs.answerPlaintiff
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
            introPlaintiff = state.introPlaintiff,
            introDefendant = state.introDefendant,
            questionDefendant = state.questionDefendant,
            answerDefendant = state.answerDefendant,
            questionPlaintiff = state.questionPlaintiff,
            answerPlaintiff = state.answerPlaintiff
        ) ?: Dialogs(
            introPlaintiff = state.introPlaintiff,
            introDefendant = state.introDefendant,
            questionDefendant = state.questionDefendant,
            answerDefendant = state.answerDefendant,
            questionPlaintiff = state.questionPlaintiff,
            answerPlaintiff = state.answerPlaintiff
        )

        viewModelScope.launch {
            if (state.dialogsEmEdicao == null) {
                repository.insertDialog(dialogsParaSalvar)
            } else {
                repository.updateDialogs(dialogsParaSalvar)
            }
        }

        limparCampos()
    }

    private fun limparCampos() {
        _uiState.update {
            it.copy(
                introPlaintiff = "",
                introDefendant = "",
                questionDefendant = "",
                answerDefendant = "",
                questionPlaintiff = "",
                answerPlaintiff = "",
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
