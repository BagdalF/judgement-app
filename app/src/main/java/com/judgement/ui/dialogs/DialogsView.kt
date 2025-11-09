package com.judgement.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.data.local.Dialogs

@Composable
fun DialogsView(
    dialogViewModel: DialogsViewModel
) {
    val uiState by dialogViewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Botão Novo Diálogo
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Novo Diálogo")
        }

        // Lista de Diálogos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.listaDeDialogs) { dialog ->
                DialogCard(
                    dialog = dialog,
                    onEdit = {
                        dialogViewModel.onEditar(dialog)
                        showDialog = true
                    },
                    onDelete = { dialogViewModel.onDeletar(dialog.id) }
                )
            }
        }
    }

    // Modal de Edição/Criação
    if (showDialog) {
        DialogForm(
            uiState = uiState,
            onDismiss = { showDialog = false },
            onSave = {
                dialogViewModel.onSalvar()
                showDialog = false
            },
            onIntroPlaintiffChange = dialogViewModel::onIntroPlaintiffChange,
            onIntroDefendantChange = dialogViewModel::onIntroDefendantChange,
            onQuestionDefendantChange = dialogViewModel::onQuestionDefendantChange,
            onAnswerDefendantChange = dialogViewModel::onAnswerDefendantChange,
            onQuestionPlaintiffChange = dialogViewModel::onQuestionPlaintiffChange,
            onAnswerPlaintiffChange = dialogViewModel::onAnswerPlaintiffChange
        )
    }
}

@Composable
fun DialogCard(
    dialog: Dialogs,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Autor:", fontWeight = FontWeight.Bold)
            Text(dialog.introPlaintiff)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Réu:", fontWeight = FontWeight.Bold)
            Text(dialog.introDefendant)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Pergunta ao Réu:", fontWeight = FontWeight.Bold)
            Text(dialog.questionDefendant)
            Text("Resposta:", fontWeight = FontWeight.Bold)
            Text(dialog.answerDefendant)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Pergunta ao Autor:", fontWeight = FontWeight.Bold)
            Text(dialog.questionPlaintiff)
            Text("Resposta:", fontWeight = FontWeight.Bold)
            Text(dialog.answerPlaintiff)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
                TextButton(onClick = onDelete) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Composable
fun DialogForm(
    uiState: DialogsUiState,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onIntroPlaintiffChange: (String) -> Unit,
    onIntroDefendantChange: (String) -> Unit,
    onQuestionDefendantChange: (String) -> Unit,
    onAnswerDefendantChange: (String) -> Unit,
    onQuestionPlaintiffChange: (String) -> Unit,
    onAnswerPlaintiffChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (uiState.dialogsEmEdicao == null) "Novo Diálogo" else "Editar Diálogo") },
        text = {
            Column {
                OutlinedTextField(
                    value = uiState.introPlaintiff,
                    onValueChange = onIntroPlaintiffChange,
                    label = { Text("Introdução do Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.introDefendant,
                    onValueChange = onIntroDefendantChange,
                    label = { Text("Introdução do Réu") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.questionDefendant,
                    onValueChange = onQuestionDefendantChange,
                    label = { Text("Pergunta ao Réu") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.answerDefendant,
                    onValueChange = onAnswerDefendantChange,
                    label = { Text("Resposta do Réu") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.questionPlaintiff,
                    onValueChange = onQuestionPlaintiffChange,
                    label = { Text("Pergunta ao Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.answerPlaintiff,
                    onValueChange = onAnswerPlaintiffChange,
                    label = { Text("Resposta do Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text(uiState.textoBotao)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}