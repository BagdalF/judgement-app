package com.judgement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.judgement.data.local.Users

// Adicione callbacks para integrar com NavController ou actions
@Composable
fun HomeView(
    currentUser: Users,
    onNewCase: () -> Unit,
    onManageUsers: () -> Unit = {},
    onManagePersons: () -> Unit = {},
    onManageDialogs: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Text(
            text = "Welcome, ${currentUser.username}, to",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "The Judgement",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        ElevatedButton(
            onClick = onNewCase,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Play", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }

        if (currentUser.isAdmin) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onManageUsers,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Manage Users"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Manage Users", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = onManagePersons,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Manage Persons"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Manage Persons", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            OutlinedButton(
                onClick = onManageDialogs,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Manage Dialogs"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("Manage Dialogs", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// --- Preview para design rápido ---
@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    val mockUser = Users(
        id = 1,
        username = "João Silva",
        email = "",
        password = "",
        isAdmin = true
    )

    HomeView(
        currentUser = mockUser,
        onNewCase = { /* navegar para criar caso */ }
    )
}
