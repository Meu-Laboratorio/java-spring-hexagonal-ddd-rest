package com.gusrubin.lab.taskmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.User
import com.gusrubin.lab.taskmanager.data.model.UserWithTasks
import com.gusrubin.lab.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

// Define os possíveis estados da UI para simplificar o gerenciamento na View
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // StateFlow para expor os dados do usuário e suas tarefas para a UI
    private val _userState = MutableStateFlow<UiState<UserWithTasks>>(UiState.Loading)
    val userState: StateFlow<UiState<UserWithTasks>> = _userState

    // Exemplo de como buscar os dados de um usuário
    fun fetchUser(userId: String) {
        viewModelScope.launch {
            _userState.value = UiState.Loading
            try {
                val response = repository.getUser(userId)
                if (response.isSuccessful && response.body() != null) {
                    _userState.value = UiState.Success(response.body()!!)
                } else {
                    _userState.value = UiState.Error("Erro ao buscar usuário: ${response.message()}")
                }
            } catch (t: Throwable) {
                // Captura QUALQUER erro (Exception, Error, etc.) para diagnóstico
                t.printStackTrace()
                val errorMessage = "Tipo: ${t::class.simpleName}\nMsg: ${t.message}"
                _userState.value = UiState.Error(errorMessage)
            }
        }
    }

    // Função genérica para executar ações (POST, PATCH, DELETE) e atualizar a UI
    // O `onSuccess` é um callback para ser executado após o sucesso da operação
    fun executeTaskAction(action: suspend () -> Response<*>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = action()
                if (response.isSuccessful) {
                    onSuccess() // Executa o callback de sucesso
                } else {
                    // Aqui você poderia expor um StateFlow de erro para a UI
                    println("Erro na operação: ${response.message()}")
                }
            } catch (e: Exception) {
                // Tratar exceções de rede
                println("Falha na conexão: ${e.message}")
            }
        }
    }

    fun createTask(userId: String, task: Task) {
        executeTaskAction(
            action = { repository.createTask(userId, task) },
            onSuccess = { fetchUser(userId) } // Após criar, atualiza a lista de tarefas
        )
    }

    fun updateUser(userId: String, taskId: String, task: Task) {
        executeTaskAction(
            action = { repository.updateTask(userId, taskId, task) },
            onSuccess = { fetchUser(userId) } // Após atualizar, recarrega os dados
        )
    }

    fun deleteTask(userId: String, taskId: String) {
        executeTaskAction(
            action = { repository.deleteTask(userId, taskId) },
            onSuccess = { fetchUser(userId) } // Após deletar, recarrega os dados
        )
    }

    // ... outras funções como createUser, deleteUser ...
}