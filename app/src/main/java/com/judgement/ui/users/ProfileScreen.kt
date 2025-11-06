package com.judgement.ui.users

// ui/screens/ProfileScreen.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    userViewModel: UsersViewModel,
    user: FirebaseUser,
    onSignOut: () -> Unit,
) {
    var newDisplayName by remember { mutableStateOf(user.displayName ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Nome: ${user.displayName ?: "Não definido"}")
        Text("Email: ${user.email}")
        Spacer(modifier = Modifier.height(24.dp))

        Text("Editar Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newDisplayName,
            onValueChange = { newDisplayName = it },
            label = { Text("Novo nome de exibição") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                authViewModel.updateUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar Nome")
        }
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.signOut(userState = userViewModel)
                onSignOut()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout (Sair)")
        }
    }
}