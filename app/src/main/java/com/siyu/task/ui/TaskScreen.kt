package com.siyu.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.siyu.task.ui.components.labelRes
import com.siyu.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel(),
    animateItemPlacement: Modifier.Companion.() -> Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var text by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    // Cierra el diálogo, vacía el texto y quita el error.
    // Se usa al cancelar, al tocar fuera y al agregar con éxito.
    val closeDialog = {
        showDialog = false
        text = ""
        viewModel.onTitleChanged()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            if (state.visibleTasks.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_list),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    // Espacio abajo para que el botón flotante no tape la última tarea
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    state.groupedTasks.forEach { category, tasks ->
                        item(key = "header_$category") {
                            Text(
                                text = stringResource(category.labelRes),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        items(items = tasks, key = { it.id }) { task ->
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { closeDialog() },
            title = { Text(text = stringResource(R.string.add_task)) },
            text = {
                TaskInput(
                    text = text,
                    onTextChange = {
                        text = it
                        viewModel.onTitleChanged()
                    },
                    showError = state.showError,
                    selectedPriority = state.selectedPriority,
                    onPrioritySelected = { viewModel.onPrioritySelected(it) },
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { viewModel.onCategorySelected(it) }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val added = viewModel.onAddTask(text)
                        if (added) {
                            closeDialog()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.add_task))
                }
            },
            dismissButton = {
                TextButton(onClick = { closeDialog() }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            textContentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}