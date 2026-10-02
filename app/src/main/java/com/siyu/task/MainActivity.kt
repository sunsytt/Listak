package com.siyu.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.siyu.task.ui.theme.TaskTheme
import model.Priority
import model.Task
import ui.SummaryCard
import ui.TaskItem
import ui.TaskScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryCardPreview() {
    MaterialTheme {
        SummaryCard(pendingCount = 2, completedCount = 1, progressPercent = 33)
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskItemPreview() {
    MaterialTheme {
        TaskItem(
            task = Task(id = 1, title = "Estudiar Compose", priority = Priority.ALTA),
            onToggle = {},
            onDelete = {}
        )
    }
}

