package com.judgement.ui.cases

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.data.local.Cases
import com.judgement.data.local.Persons
import com.judgement.ui.persons.PersonsViewModel

@Composable
fun NewCaseView(
    caseViewModel: CasesViewModel,
    personsViewModel: PersonsViewModel,
    onAcceptCase: (Cases) -> Unit,
    onRejectCase: () -> Unit
) {
    val casesState by caseViewModel.uiState.collectAsStateWithLifecycle()
    val personsState by personsViewModel.uiState.collectAsStateWithLifecycle()

    // Gera um novo caso quando a tela é carregada
    LaunchedEffect(Unit) {
        caseViewModel.generateNewCase(personsState.listaDePersons)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Novo Caso",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        casesState.currentCase?.let { case ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        "Crime: ${case.crime}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Informações do Autor:", fontWeight = FontWeight.Bold)
                    casesState.currentPlaintiff?.let { person ->
                        PersonInfoSection(person)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Informações do Réu:", fontWeight = FontWeight.Bold)
                    casesState.currentDefendant?.let { person ->
                        PersonInfoSection(person)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onRejectCase() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Recusar Caso")
                }

                Button(
                    onClick = { onAcceptCase(case) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Aceitar Caso")
                }
            }
        }
    }
}

@Composable
fun PersonInfoSection(person: Persons) {
    Column(
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
    ) {
        Text("Nome: ${person.name}")
        Text("Idade: ${person.age} anos")
        Text("Endereço: ${person.address}")
        Text("Telefone: ${person.phone}")
        Text("Email: ${person.email}")
    }
}
