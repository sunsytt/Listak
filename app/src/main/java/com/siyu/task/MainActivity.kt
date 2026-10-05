package com.siyu.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.siyu.task.ui.theme.TaskTheme
import com.siyu.task.model.Priority
import com.siyu.task.model.Task
import com.siyu.task.ui.TaskScreen
import com.siyu.task.ui.components.SummaryCard
import com.siyu.task.ui.components.TaskItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskTheme {
                TaskScreen ()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryCardPreview() {
    TaskTheme {
        SummaryCard(pendingCount = 2, completedCount = 1, progressPercent = 33)
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskItemPreview() {
    TaskTheme {
        TaskItem(
            task = Task(
                id = 1,
                title = "Ejemplo de tarea",
                priority = Priority.MEDIA,
                done = false
            ),
            onToggle = {},
            onDelete = {}
        )
    }
}
