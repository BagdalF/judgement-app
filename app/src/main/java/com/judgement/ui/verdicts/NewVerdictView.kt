package com.judgement.ui.verdicts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.currentUser
import com.judgement.data.local.Dialogs
import com.judgement.ui.cases.CasesViewModel
import com.judgement.ui.dialogs.DialogsViewModel

data class ChatMessage(
    val text: String,
    val sender: String,
    val senderType: SenderType
)

enum class SenderType {
    JUDGE, PLAINTIFF, DEFENDANT
}

@Composable
fun NewVerdictView(
    casesViewModel: CasesViewModel,
    dialogsViewModel: DialogsViewModel,
    verdictsViewModel: VerdictsViewModel,
    onVerdictDelivered: () -> Unit
) {
    var showVerdictDialog by remember { mutableStateOf(false) }

    val casesState by casesViewModel.uiState.collectAsStateWithLifecycle()
    val verdictState by verdictsViewModel.uiState.collectAsStateWithLifecycle()

    var dialog by remember { mutableStateOf<Dialogs?>(null) }
    var messages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }

    // Load dialog and persons
    LaunchedEffect(casesState.currentCase) {
        val dialogsState = dialogsViewModel.uiState.value
        
        dialog = dialogsState.listaDeDialogs.find { it.id == casesState.currentCase?.idDialog }

        // Create chat messages sequence
        dialog?.let { d ->
            messages = listOf(
                ChatMessage(d.introPlaintiff, casesState.currentPlaintiff?.name ?: "", SenderType.PLAINTIFF),
                ChatMessage(d.introDefendant, casesState.currentDefendant?.name ?: "", SenderType.DEFENDANT),
                ChatMessage(d.questionDefendant, currentUser?.username ?: "Judge", SenderType.JUDGE),
                ChatMessage(d.answerDefendant, casesState.currentDefendant?.name ?: "", SenderType.DEFENDANT),
                ChatMessage(d.questionPlaintiff, currentUser?.username ?: "Judge", SenderType.JUDGE),
                ChatMessage(d.answerPlaintiff, casesState.currentPlaintiff?.name ?: "", SenderType.PLAINTIFF)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECE5DD))
    ) {
        // Chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }

        // Deliver verdict button
        Button(
            onClick = { showVerdictDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Deliver Verdict", fontSize = 18.sp)
        }
    }

    if (showVerdictDialog) {
        VerdictDialog(
            verdictsViewModel = verdictsViewModel,
            verdictState = verdictState,
            onDismiss = { showVerdictDialog = false },
            onVerdict = {
                verdictsViewModel.onSalvar(
                    idCase = casesState.currentCase?.id ?: 0,
                    idUser = currentUser?.id ?: 0,
                    isGuilty = verdictState.isGuilty,
                    prisonYears = verdictState.prisonYears,
                    fineAmount = verdictState.fineAmount
                )
                onVerdictDelivered()
            }
        )
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val alignment = when (message.senderType) {
        SenderType.JUDGE -> Alignment.End
        else -> Alignment.Start
    }
    
    val backgroundColor = when (message.senderType) {
        SenderType.JUDGE -> Color(0xFF128C7E)
        SenderType.PLAINTIFF -> Color(0xFF1976D2)
        SenderType.DEFENDANT -> Color(0xFFE53935)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Text(
            text = message.sender,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.senderType == SenderType.JUDGE) 8.dp else 0.dp,
                        topEnd = if (message.senderType != SenderType.JUDGE) 8.dp else 0.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
                .background(backgroundColor)
                .padding(8.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White
            )
        }
    }
}

@Composable
fun VerdictDialog(
    verdictsViewModel: VerdictsViewModel,
    verdictState: VerdictsUiState,
    onDismiss: () -> Unit,
    onVerdict: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Deliver Verdict") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { verdictsViewModel.onIsGuiltyChange(false) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!verdictState.isGuilty) Color.Green else Color.Gray
                        )
                    ) {
                        Text("Innocent")
                    }
                    Button(
                        onClick = { verdictsViewModel.onIsGuiltyChange(true) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (verdictState.isGuilty) Color.Red else Color.Gray
                        )
                    ) {
                        Text("Guilty")
                    }
                }

                if (verdictState.isGuilty) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = verdictState.prisonYears,
                        onValueChange = { verdictsViewModel.onPrisonYearsChange(it) },
                        label = { Text("Prison Years") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = verdictState.fineAmount.toString(),
                        onValueChange = { verdictsViewModel.onFineAmountChange(it.toIntOrNull() ?: 0) },
                        label = { Text("Fine Amount (R$)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onVerdict()
                onDismiss()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
