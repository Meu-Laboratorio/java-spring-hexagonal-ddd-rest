package com.gusrubin.lab.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.gusrubin.lab.taskmanager.data.network.RetrofitInstance
import com.gusrubin.lab.taskmanager.data.repository.TaskRepository
import com.gusrubin.lab.taskmanager.ui.SignUpScreen
import com.gusrubin.lab.taskmanager.ui.theme.TaskManagerTheme
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModel
import com.gusrubin.lab.taskmanager.ui.viewmodel.TaskViewModelFactory

class SignUpActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(RetrofitInstance.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                val creationState by viewModel.userCreationState.collectAsState()
                SignUpScreen(
                    creationState = creationState,
                    onSignUpClick = { name ->
                        viewModel.createUser(name)
                    }
                )
            }
        }
    }
}