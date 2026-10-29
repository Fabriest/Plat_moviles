package com.moviles.lab8.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.moviles.lab8.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.weight(1f))

        AsyncImage(
            model = "https://upload.wikimedia.org/wikipedia/commons/b/b1/Rick_and_Morty.svg",
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.login(name) {
                        onLoginSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            enabled = name.isNotBlank() && !isLoading
        ) {
            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar sesión")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "Fabricio Estrada - 25230", modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = viewModel(),
    onLogoutClick: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(text = userName ?: "", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Carné: 25230", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                viewModel.logout {
                    onLogoutClick()
                }
            },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión")
        }
    }
}