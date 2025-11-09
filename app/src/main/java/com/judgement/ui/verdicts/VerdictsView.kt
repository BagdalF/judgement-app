package com.judgement.ui.verdicts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun VerdictsView(
    verdictViewModel: VerdictsViewModel
) {
    val uiState by verdictViewModel.uiState.collectAsStateWithLifecycle()

    verdictViewModel.loadVerdicts()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Lista de Diálogos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.verdictsWithCases) { verdictWithCase ->
                VerdictCard(
                    verdictWithCase = verdictWithCase,
                    onDelete = {
                        verdictViewModel.onDeletar(
                            verdictWithCase.verdict.id,
                            verdictWithCase.case?.id ?: verdictWithCase.verdict.idCase
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun VerdictCard(
    verdictWithCase: VerdictWithCase,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Caso
            verdictWithCase.case?.let { case ->
                Text("Crime:", fontWeight = FontWeight.Bold)
                Text(case.crime)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Pessoas envolvidas
            Text("Pessoas Envolvidas:", fontWeight = FontWeight.Bold)
            verdictWithCase.defendant?.let { defendant ->
                Text("Réu: ${defendant.name}")
            }
            verdictWithCase.plaintiff?.let { plaintiff ->
                Text("Autor: ${plaintiff.name}")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Veredicto
            Text("Veredicto:", fontWeight = FontWeight.Bold)
            with(verdictWithCase.verdict) {
                Text("Decisão: ${if (isGuilty) "Culpado" else "Inocente"}")
                if (isGuilty) {
                    Text("Pena: $prisonYears anos")
                    Text("Multa: R$ $fineAmount")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDelete) {
                    Text("Excluir")
                }
            }
        }
    }
}