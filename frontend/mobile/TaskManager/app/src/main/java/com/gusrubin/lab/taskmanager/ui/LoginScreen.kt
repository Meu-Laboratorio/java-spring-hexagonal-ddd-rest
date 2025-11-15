package com.gusrubin.lab.taskmanager.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gusrubin.lab.taskmanager.MainActivity
import com.gusrubin.lab.taskmanager.SignUpActivity

@Composable
fun LoginScreen() {
    var userId by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo ao TaskManager", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("ID do Usuário") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (userId.isNotBlank()) {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("USER_ID", userId)
                    }
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar com ID")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                context.startActivity(Intent(context, SignUpActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar Novo Usuário")
        }
    }
}
