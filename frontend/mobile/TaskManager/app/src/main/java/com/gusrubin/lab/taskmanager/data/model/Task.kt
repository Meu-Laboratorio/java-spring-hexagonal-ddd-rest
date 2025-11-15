package com.gusrubin.lab.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("isDone") val isDone: Boolean
)