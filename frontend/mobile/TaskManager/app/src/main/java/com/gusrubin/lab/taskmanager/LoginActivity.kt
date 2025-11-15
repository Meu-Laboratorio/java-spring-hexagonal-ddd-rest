package com.gusrubin.lab.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gusrubin.lab.taskmanager.ui.LoginScreen
import com.gusrubin.lab.taskmanager.ui.theme.TaskManagerTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                LoginScreen()
            }
        }
    }
}