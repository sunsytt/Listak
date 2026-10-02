package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import viewmodel.TaskViewModel
import com.siyu.task.R
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.siyu.task.uicomponents.TaskInput
import uicomponents.TaskInput

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel(),
    isEmpty: Any.() -> Boolean
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var text by rememberSaveable { mutableStateOf("") }

    TaskInput(
        text = text,
        onTextChange = {
            text = it
            viewModel.onTitleChanged()
        },
        showError = state.showError,
        selectedPriority = state.selectedPriority,
        onPrioritySelected = viewModel::onPrioritySelected,
        onAdd = {
            viewModel.onAddTask(text)
            text = ""
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.screen_title),
            style = MaterialTheme.typography.headlineMedium
        )
        SummaryCard(
            pendingCount = state.pendingCount,
            completedCount = state.completedCount,
            progressPercent = state.progressPercent
        )
        TaskInput(
            text = text,
            onTextChange = {
                text = it
                viewModel.onTitleChanged()
            },
            showError = state.showError,
            selectedPriority = state.selectedPriority,
            onPrioritySelected = viewModel::onPrioritySelected,
            onAdd = {
                viewModel.onAddTask(text)
                text = ""
            }
        )
        if (state.visibleTasks.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_list),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = state.visibleTasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.onToggleTask(task.id) },
                        onDelete = { viewModel.onDeleteTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCard(pendingCount: Int, completedCount: Int, progressPercent: Int) {
    TODO("Not yet implemented")
}

@Composable
fun TaskItem(task: ERROR, onToggle: () -> onToggleTask, onDelete: () -> onDeleteTask) {
    TODO("Not yet implemented")
}

fun viewModel(): TaskViewModel {}
