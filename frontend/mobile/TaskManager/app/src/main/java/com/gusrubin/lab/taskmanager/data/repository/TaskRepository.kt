package com.gusrubin.lab.taskmanager.data.repository

import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.User
import com.gusrubin.lab.taskmanager.data.network.ApiService

class TaskRepository(private val apiService: ApiService) {

    suspend fun createUser(user: User) = apiService.createUser(user)

    suspend fun getUser(userId: String) = apiService.getUser(userId)

    suspend fun deleteUser(userId: String) = apiService.deleteUser(userId)

    suspend fun createTask(userId: String, task: Task) = apiService.createTask(userId, task)

    suspend fun updateTask(userId: String, taskId: String, task: Task) =
        apiService.updateTask(userId, taskId, task)

    suspend fun deleteTask(userId: String, taskId: String) = apiService.deleteTask(userId, taskId)

}