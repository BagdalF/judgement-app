package com.judgement.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.data.local.Users

@Composable
fun UsersView(
    userViewModel: UsersViewModel
) {
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Lista de Diálogos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.listaDeUsers) { user ->
                UserCard(
                    user = user,
                    onDelete = { userViewModel.onDeletar(user.id) }
                )
            }
        }
    }
}

@Composable
fun UserCard(
    user: Users,
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
            Text("Username:", fontWeight = FontWeight.Bold)
            Text(user.username)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Email:", fontWeight = FontWeight.Bold)
            Text(user.email)
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Admin:", fontWeight = FontWeight.Bold)
            Text(if (user.isAdmin) "Sim" else "Não")
            
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