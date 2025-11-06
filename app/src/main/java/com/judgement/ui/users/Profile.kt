//package com.judgement.ui.users
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.judgement.data.local.AppDatabase
//
//@Composable
//fun EditProfileScreen(
//    profile: Usuarios,
//    viewModel: UsuariosViewModel =
//        viewModel(factory = UsuariosViewModelFactory(
//            UsuariosRepository(AppDatabase.getDatabase(LocalContext.current).usuariosDAO())
//        )
//        )
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Icon(
//                imageVector = Icons.Default.AccountCircle,
//                contentDescription = "Avatar",
//                tint = Color(0xFF1976D2),
//                modifier = Modifier.size(100.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("${profile.firstName} ${profile.lastName}", fontSize = 20.sp)
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            OutlinedTextField(
//                value = uiState.firstName,
//                onValueChange = { viewModel.onFirstNameChange(it) },
//                label = { Text("First name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = uiState.lastName,
//                onValueChange = { viewModel.onLastNameChange(it) },
//                label = { Text("Last name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = uiState.phone,
//                onValueChange = { viewModel.onPhoneChange(it) },
//                label = { Text("Phone number") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = uiState.email,
//                onValueChange = { viewModel.onEmailChange(it) },
//                label = { Text("Email") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = { viewModel.onEditar(profile) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF1976D2)
//                )
//            ) {
//                Text("Save", color = Color.White, fontSize = 16.sp)
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            DeleteAccountButton {viewModel.onDeletar(profile.id)}
//        }
//    }
//}
//
//@Composable
//fun DeleteAccountButton(onDelete: () -> Unit) {
//    var showDialog by remember { mutableStateOf(false) }
//
//    // Botão que abre o modal
//    Button(
//        onClick = { showDialog = true },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Red
//        )
//    ) {
//        Text("Excluir Conta", color = Color.White, fontSize = 16.sp)
//    }
//
//    // Modal de confirmação
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text(text = "Tem certeza?") },
//            text = { Text("Essa ação irá excluir permanentemente sua conta. Deseja continuar?") },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        showDialog = false
//                        onDelete()  // Chama a função de exclusão real
//                    }
//                ) {
//                    Text("Sim", color = Color.Red)
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = { showDialog = false }
//                ) {
//                    Text("Cancelar")
//                }
//            }
//        )
//    }
//}
