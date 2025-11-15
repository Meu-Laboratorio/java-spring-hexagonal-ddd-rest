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
import com.gusrubin.lab.taskmanager.data.model.User
import com.gusrubin.lab.taskmanager.ui.viewmodel.UiState

@Composable
fun SignUpScreen(creationState: UiState<User>, onSignUpClick: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Observa o estado da criação do usuário para navegar em caso de sucesso
    LaunchedEffect(creationState) {
        if (creationState is UiState.Success) {
            val intent = Intent(context, MainActivity::class.java).apply {
                // Corrigido: Converte o ID (Long) para String para a Intent
                putExtra("USER_ID", creationState.data.id.toString())
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Criar Novo Usuário", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Usuário") },
            modifier = Modifier.fillMaxWidth(),
            isError = creationState is UiState.Error
        )

        if (creationState is UiState.Error) {
            Text(
                text = "Erro: ${creationState.message}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSignUpClick(name) },
            modifier = Modifier.fillMaxWidth(),
            // Corrigido: O botão fica habilitado no estado Idle, desabilitado apenas no Loading
            enabled = creationState !is UiState.Loading
        ) {
            when (creationState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
                else -> {
                    Text("Cadastrar")
                }
            }
        }
    }
}