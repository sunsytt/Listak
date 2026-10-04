package com.siyu.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siyu.task.R
import com.siyu.task.ui.components.SummaryCard
import com.siyu.task.ui.components.TaskInput
import com.siyu.task.ui.components.TaskItem
import com.siyu.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var text by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name))},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddTask(text) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_task))
            }
        }
    ){ paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
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
}

