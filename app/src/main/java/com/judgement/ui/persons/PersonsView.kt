package com.judgement.ui.persons

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.data.local.Persons

// Adicione callbacks para integrar com NavController ou actions
@Composable
fun PersonsView(
    personViewModel: PersonsViewModel
) {
    val uiState by personViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Lista de Pessoas
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.listaDePersons) { person ->
                PersonCard(
                    person = person,
                    onDelete = { personViewModel.onDeletar(person.id) }
                )
            }
        }
    }
}

@Composable
fun PersonCard(
    person: Persons,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Nome:", fontWeight = FontWeight.Bold)
            Text(person.name)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Endereço:", fontWeight = FontWeight.Bold)
            Text(person.address)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Telefone:", fontWeight = FontWeight.Bold)
            Text(person.phone)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Email:", fontWeight = FontWeight.Bold)
            Text(person.email)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Idade:", fontWeight = FontWeight.Bold)
            Text(person.age.toString())
            
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

@Preview(showBackground = true)
@Composable
fun PersonsViewPreview() {
    // Preview implementation
}
