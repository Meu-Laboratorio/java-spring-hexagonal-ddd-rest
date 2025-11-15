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

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<UserWithTasks>>(UiState.Idle)
    val userState: StateFlow<UiState<UserWithTasks>> = _userState

    private val _userCreationState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val userCreationState: StateFlow<UiState<User>> = _userCreationState

    fun fetchUser(userId: Long) {
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
                t.printStackTrace()
                _userState.value = UiState.Error("Falha na conexão: ${t.message}")
            }
        }
    }

    fun createUser(name: String) {
        viewModelScope.launch {
            _userCreationState.value = UiState.Loading
            try {
                val response = repository.createUser(User(id = 0L, name = name, email = ""))
                if (response.isSuccessful && response.body() != null) {
                    _userCreationState.value = UiState.Success(response.body()!!)
                } else {
                    _userCreationState.value = UiState.Error("Erro ao criar usuário: ${response.message()}")
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                _userCreationState.value = UiState.Error("Falha na conexão: ${t.message}")
            }
        }
    }

    private fun executeTaskAction(action: suspend () -> Response<*>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = action()
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    println("Erro na operação: ${response.message()}")
                }
            } catch (e: Exception) {
                println("Falha na conexão: ${e.message}")
            }
        }
    }

    fun createTask(userId: Long, task: Task) {
        executeTaskAction(
            action = { repository.createTask(userId, task) },
            onSuccess = { fetchUser(userId) }
        )
    }

    fun updateUser(userId: Long, task: Task) {
        executeTaskAction(
            action = { repository.updateTask(userId, task.id.toString(), task) },
            onSuccess = { fetchUser(userId) }
        )
    }

    fun deleteTask(userId: Long, taskId: Long) {
        executeTaskAction(
            action = { repository.deleteTask(userId, taskId.toString()) },
            onSuccess = { fetchUser(userId) }
        )
    }
}