package com.gusrubin.lab.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class UserWithTasks(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("tasks") val tasks: List<Task>
)