package com.gusrubin.lab.taskmanager.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    // O campo pode ser nulo ao criar uma nova tarefa
    @SerializedName("scheduledDateTime") val scheduledDateTime: String?,
    @SerializedName("done") val done: Boolean
)
