package com.gusrubin.lab.taskmanager.data.network

import com.gusrubin.lab.taskmanager.data.model.Task
import com.gusrubin.lab.taskmanager.data.model.User
import com.gusrubin.lab.taskmanager.data.model.UserWithTasks
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // POST /users
    @POST("users")
    suspend fun createUser(@Body user: User): Response<User>

    // GET /users/{userId}
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserWithTasks>

    // DELETE /users/{userId}
    @DELETE("users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): Response<Unit>

    // POST /users/{userId}/tasks
    @POST("users/{userId}/tasks")
    suspend fun createTask(@Path("userId") userId: String, @Body task: Task): Response<Task>

    // PATCH /users/{userId}/tasks/{taskId}
    @PATCH("users/{userId}/tasks/{taskId}")
    suspend fun updateTask(
        @Path("userId") userId: String,
        @Path("taskId") taskId: String,
        @Body task: Task // Supondo que você envie o objeto Task completo para atualização
    ): Response<Task>

    // DELETE /users/{userId}/tasks/{taskId}
    @DELETE("users/{userId}/tasks/{taskId}")
    suspend fun deleteTask(
        @Path("userId") userId: String,
        @Path("taskId") taskId: String
    ): Response<Unit>
}