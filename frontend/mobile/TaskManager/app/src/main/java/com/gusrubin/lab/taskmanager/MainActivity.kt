package com.gusrubin.lab.taskmanager

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.UserWithTasks
import com.gusrubin.lab.taskmanager.data.network.RetrofitInstance
import com.gusrubin.lab.taskmanager.data.repository.TaskRepository
import com.gusrubin.lab.taskmanager.ui.theme.TaskManagerTheme
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModel
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModelFactory
import com.gusrubin.lab.taskmanager.ui.viewmodel.UiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userIdString = intent.getStringExtra("USER_ID")
        val userId = userIdString?.toLongOrNull()

        setContent {
            TaskManagerTheme {
                val userState by viewModel.userState.collectAsState()

                if (userId != null) {
                    LaunchedEffect(key1 = userId) {
                        viewModel.fetchUser(userId)
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (userId != null) {
                        TaskScreen(
                            uiState = userState,
                            onAddTask = { title, scheduledDateTime ->
                                val newTask = Task(
                                    id = 0L,
                                    title = title,
                                    scheduledDateTime = scheduledDateTime,
                                    done = false
                                )
                                viewModel.createTask(userId, newTask)
                            },
                            onUpdateTask = { task ->
                                viewModel.updateUser(userId, task)
                            },
                            onDeleteTask = { taskId ->
                                viewModel.deleteTask(userId, taskId)
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Erro: ID do usuário inválido ou não fornecido.")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    uiState: UiState<UserWithTasks>,
    onAddTask: (String, String?) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Long) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciador de Tarefas") },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is UiState.Idle -> { /* Não mostra nada */ }
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
    }
}


@Composable
fun TaskListContent(
    userWithTasks: UserWithTasks,
    onAddTask: (String, String?) -> Unit,
    onUpdateTask: (Task) -> Unit,
    onDeleteTask: (Long) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var scheduledDateTime by remember { mutableStateOf<LocalDateTime?>(null) }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            scheduledDateTime = scheduledDateTime?.withHour(selectedHour)?.withMinute(selectedMinute)
        },
        hour, minute, true
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear, selectedMonth, selectedDayOfMonth ->
            scheduledDateTime = LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDayOfMonth, 0, 0)
            timePickerDialog.show()
        },
        year, month, day
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Tarefas de ${userWithTasks.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Título da Tarefa") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(Icons.Default.DateRange, contentDescription = "Selecionar Prazo")
            }
        }

        scheduledDateTime?.let {
            Text(
                text = "Prazo: ${it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (text.isNotBlank()) {
                val formattedDateTime = scheduledDateTime?.format(DateTimeFormatter.ISO_DATE_TIME)
                onAddTask(text, formattedDateTime)
                text = ""
                scheduledDateTime = null
            }
        }) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(userWithTasks.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onUpdateTask = onUpdateTask,
                    onDeleteClick = { onDeleteTask(task.id) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onUpdateTask: (Task) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onUpdateTask(task.copy(done = !task.done)) }
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, fontSize = 18.sp)
            task.scheduledDateTime?.let {
                val formattedDateTime = remember(it) {
                    try {
                        LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME)
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    } catch (e: DateTimeParseException) {
                        null
                    }
                }
                formattedDateTime?.let {
                    Text(
                        text = "Prazo: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Deletar Tarefa")
        }
    }
}