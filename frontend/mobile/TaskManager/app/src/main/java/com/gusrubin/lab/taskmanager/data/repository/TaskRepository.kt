package com.gusrubin.lab.taskmanager.data.repository

import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.User
import com.gusrubin.lab.taskmanager.data.network.ApiService

class TaskRepository(private val apiService: ApiService) {

    suspend fun createUser(user: User) = apiService.createUser(user)

    suspend fun getUser(userId: Long) = apiService.getUser(userId.toString())

    suspend fun deleteUser(userId: Long) = apiService.deleteUser(userId.toString())

    suspend fun createTask(userId: Long, task: Task) = apiService.createTask(userId.toString(), task)

    suspend fun updateTask(userId: Long, taskId: String, task: Task) =
        apiService.updateTask(userId.toString(), taskId, task)

    suspend fun deleteTask(userId: Long, taskId: String) = apiService.deleteTask(userId.toString(), taskId)

}