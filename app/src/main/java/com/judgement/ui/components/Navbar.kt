package com.judgement.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NavBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(70.dp)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.DarkGray,
                unselectedIconColor = Color.DarkGray,
                unselectedTextColor = Color.DarkGray,
                indicatorColor = Color(0xB21976D2)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Statement") },
            label = { Text("Statement") },
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.DarkGray,
                unselectedIconColor = Color.DarkGray,
                unselectedTextColor = Color.DarkGray,
                indicatorColor = Color(0xFF1976D2)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Info, contentDescription = "Transfers") },
            label = { Text("Transfers") },
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.DarkGray,
                unselectedIconColor = Color.DarkGray,
                unselectedTextColor = Color.DarkGray,
                indicatorColor = Color(0xFF1976D2)
            )
        )
    }
}
