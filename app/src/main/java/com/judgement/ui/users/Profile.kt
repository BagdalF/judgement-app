package com.judgement.ui.users

// ui/views/Profile.kt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.judgement.data.local.Users

@Composable
fun ProfileView(
    profile: Users,
    userViewModel: UsersViewModel,
    authViewModel: AuthViewModel,
    onSignOut: () -> Unit
) {
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(profile) {
        userViewModel.onUsernameChange(profile.username)
        userViewModel.onEmailChange(profile.email)
        userViewModel.onPasswordChange(profile.password)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { userViewModel.onUsernameChange(it) },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { userViewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { userViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    userViewModel.onEditar(profile)
                    userViewModel.onSalvar(profile.firebaseId ?: "")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.signOut()
                    onSignOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Logout", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            DeleteAccountButton {
                userViewModel.onDeletar(profile.id)
                authViewModel.signOut()
                onSignOut()
            }
        }
    }
}

@Composable
fun DeleteAccountButton(onDelete: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    // Botão que abre o modal
    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Excluir Conta", color = MaterialTheme.colorScheme.onError, fontSize = 16.sp)
    }

    // Modal de confirmação
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Tem certeza?") },
            text = { Text("Essa ação irá excluir permanentemente sua conta. Deseja continuar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onDelete()
                    }
                ) {
                    Text("Sim", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}