//package com.judgement.ui.users
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.judgement.data.local.AppDatabase
//
//@Composable
//fun RegisterScreen(
//    viewModel: UsuariosViewModel =
//        viewModel(factory = UsuariosViewModelFactory(
//            UsuariosRepository(AppDatabase.getDatabase(LocalContext.current).usuariosDAO())
//        )
//        )
//) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Text(
//                text = "Welcome!",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black
//            )
//            Text(
//                text = "Please sign in to continue",
//                fontSize = 16.sp,
//                color = Color.Gray
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
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
//            OutlinedTextField(
//                value = uiState.password,
//                onValueChange = { viewModel.onPasswordChange(it) },
//                label = { Text("Password") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Button(
//                onClick = {
//                    viewModel.onSalvar()
//                    Toast.makeText(context, "Logged Successfully!", Toast.LENGTH_SHORT).show()
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF1976D2)
//                )
//            ) {
//                Text("Send", color = Color.White, fontSize = 16.sp)
//            }
//        }
//        Spacer(modifier = Modifier.weight(1f))
//    }
//}