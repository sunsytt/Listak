package com.siyu.task.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siyu.task.R
import com.siyu.task.ui.components.FilterBar
import com.siyu.task.ui.components.SummaryCard
import com.siyu.task.ui.components.TaskInput
import com.siyu.task.ui.components.TaskItem
import com.siyu.task.ui.components.labelRes
import com.siyu.task.ui.theme.TaskTheme
import com.siyu.task.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
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
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
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
            FilterBar(
                selectedFilter = state.filter,
                onFilterSelected = { viewModel.onFilterSelected(it) },
                onSortChanged = { viewModel.onShortChanged(it) },
                sortByPriority = state.sortByPriority
            )
            if (state.visibleTasks.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_list),
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyVerticalGrid (
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    columns = GridCells.Adaptive(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    state.groupedTasks.forEach { category, tasks ->
                        item(key = "header_$category",
                            span = { GridItemSpan(maxLineSpan) }
                        ){
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TaskScreenPreview() {
    TaskTheme  {
        TaskScreen()
    }
}