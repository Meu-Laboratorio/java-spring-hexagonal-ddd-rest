package com.gusrubin.lab.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.UserWithTasks
import com.gusrubin.lab.taskmanager.ui.theme.TaskManagerTheme
import com.gusrubin.lab.taskmanager.ui.viewmodel.UiState
import kotlin.getValue
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModel
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModelFactory
import com.gusrubin.lab.taskmanager.data.repository.TaskRepository
import com.gusrubin.lab.taskmanager.data.network.RetrofitInstance

class MainActivity : ComponentActivity() {

    // Instancia o ViewModel usando a nossa Factory
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Exemplo: ID fixo do usuário para demonstração.
        val exampleUserId = "1"

        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                // Coleta o estado da UI a partir do ViewModel
                val userState by viewModel.userState.collectAsState()
//
//                // LaunchedEffect é a forma correta de chamar uma suspend fun
//                // uma única vez quando o Composable é exibido.
                LaunchedEffect(key1 = Unit) {
                    viewModel.fetchUser(exampleUserId)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen(
                        uiState = userState,
                        onAddTask = { description ->
                            val newTask = Task(id = "", description = description, isDone = false)
                            viewModel.createTask(exampleUserId, newTask)
                        },
                        onUpdateTask = { task ->
                            viewModel.updateUser(
                                exampleUserId,
                                task.id,
                                task.copy(isDone = !task.isDone)
                            )
                        },
                        onDeleteTask = { taskId ->
                            viewModel.deleteTask(exampleUserId, taskId)
                        }
                    )
                }

//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskManagerTheme {
        Greeting("Android")
    }
}

// Composable principal que decide o que mostrar na tela
@Composable
fun TaskScreen(
    uiState: UiState<UserWithTasks>,
    onAddTask: (String) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Erro: ${uiState.message}", color = MaterialTheme.colorScheme.error)
            }
        }

        is UiState.Success -> {
            TaskListContent(
                userWithTasks = uiState.data,
                onAddTask = onAddTask,
                onUpdateTask = onUpdateTask,
                onDeleteTask = onDeleteTask
            )
        }
    }
}

// Composable que exibe o conteúdo quando os dados foram carregados com sucesso
@Composable
fun TaskListContent(
    userWithTasks: UserWithTasks,
    onAddTask: (String) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tarefas de ${userWithTasks.name}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto e botão para adicionar nova tarefa
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Nova tarefa") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (text.isNotBlank()) {
                    onAddTask(text)
                    text = "" // Limpa o campo
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de tarefas
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(userWithTasks.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { onUpdateTask(task) },
                    onDeleteClick = { onDeleteTask(task.id) }
                )
                Divider()
            }
        }
    }
}

// Composable para um único item da lista de tarefas
@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isDone,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = task.description,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Deletar Tarefa")
        }
    }
}