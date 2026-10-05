package com.siyu.task.viewmodel

import androidx.lifecycle.ViewModel
import com.siyu.task.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.siyu.task.model.Priority
import com.siyu.task.model.Task
import com.siyu.task.model.TaskFilter
import kotlinx.coroutines.flow.update

data class TaskUiState(
    val task: List<Task> = emptyList(),
    val filter: TaskFilter = TaskFilter.TODAS,
    val sortByPriority: Boolean = false,
    val selectedPriority: Priority = Priority.MEDIA,
    val showError: Boolean = false,
    val visibleTasks: List<Task> = emptyList(),
    val pendingCount: Int = 0,
    val completedCount: Int = 0,
    val progressPercent: Int = 0,
    val selectedCategory: Category = Category.ESTUDIOS,
    val groupedTasks: Map<Category, List<Task>> = emptyMap()
) {

}


class TaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(recalculate(TaskUiState(task = mockTasks())))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow ()

    private fun recalculate(state: TaskUiState): TaskUiState {
        val filtered = when (state.filter) {
            TaskFilter.TODAS -> state.task
            TaskFilter.PENDIENTES -> state.task.filter { !it.done }
            TaskFilter.COMPLETADAS -> state.task.filter { it.done }
        }
        //ordena por prioridad
        val visible = if (state.sortByPriority) {
            filtered.sortedBy { it.priority.ordinal }
        } else {
            filtered
        }
        //agrupacion de tareas por categoria
        val grouped = visible
            .sortedBy { it.category.ordinal }
            .groupBy { it.category }

        val completed = state.task.count { it.done }
        val percent = if (state.task.isEmpty()) 0 else completed * 100 / state.task.size
        return state.copy(
            visibleTasks = visible,
            groupedTasks = grouped,
            pendingCount = state.task.size - completed,
            completedCount = completed,
            progressPercent = percent
        )
    }

    private fun mockTasks(): List<Task> = listOf(
        Task(id = 1, title = "Estudiar para examen de calculo -miercoles", priority = Priority.ALTA, category = Category.ESTUDIOS),
        Task(id = 2, title = "Repasar temas de programacion", priority = Priority.MEDIA, category = Category.ESTUDIOS),
        Task(id = 3, title = "Ir de compras el sabado", priority = Priority.BAJA, category = Category.HOGAR)
    )

    fun onTitleChanged() {
        _uiState.value = _uiState.value.copy(showError = false)
    }

    fun onPrioritySelected(priority: Priority) {
        _uiState.value = recalculate(_uiState.value.copy(selectedPriority = priority))
    }

    fun onFilterSelected(filter: TaskFilter) {
        _uiState.value = recalculate(_uiState.value.copy(filter = filter))
    }

    fun onCategorySelected(category: Category) {
        _uiState.value = recalculate(_uiState.value.copy(selectedCategory = category))
    }
    fun onAddTask(title: String): Boolean {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) {
            _uiState.update { it.copy(showError = true) }
            return false
        }
        _uiState.update { current ->
            val newId = (current.task.maxOfOrNull { it.id } ?: 0) + 1
            val newTask = Task(
                id = newId,
                title = cleanTitle,
                priority = current.selectedPriority,
                category = current.selectedCategory
            )
            recalculate(current.copy(task = current.task + newTask, showError = false))
        }
        return true
    }

    fun onToggleTask(id: Int) {
        val updatedTasks = _uiState.value.task.map {
            if (it.id == id) it.copy(done = !it.done) else it
        }
        _uiState.value = recalculate(_uiState.value.copy(task = updatedTasks))
    }

    fun onDeleteTask(id: Int) {
        val updatedTasks = _uiState.value.task.filter { it.id != id }
        _uiState.value = recalculate(_uiState.value.copy(task = updatedTasks))
    }

    fun onShortChanged(enabled: Boolean){
        _uiState.value = recalculate(_uiState.value.copy(sortByPriority = enabled))
    }

    fun onClearCompleted(){
        val updatedTasks = _uiState.value.task.filter { !it.done }
        _uiState.value = recalculate(_uiState.value.copy(task = updatedTasks))
    }
}


