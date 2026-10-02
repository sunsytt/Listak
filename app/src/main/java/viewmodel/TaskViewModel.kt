package viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Priority
import model.Task
import model.TaskFilter
import kotlinx.coroutines.flow.update

data class TaskUiState(
    val task: List<Task> = emptyList(),
    val filter: TaskFilter = TaskFilter.TODAS,
    val sortByPriority: Boolean = false,
    val selectedPriority : Priority = Priority.MEDIA,
    val showError: Boolean = false,
    //datos calclados
    val visibleTask: List<Task> = emptyList(),
    val pendingCount: Int = 0,
    val completedCount: Int = 0,
    val progressPercent: Int = 0
)

class TaskViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(recalculate(TaskUiState(task = mockTasks)))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    private fun recalculate(state: TaskUiState): TaskUiState{
        val filtered = when (state.filter){
            TaskFilter.TODAS -> state.task
            TaskFilter.PENDIENTES -> state.task.filter { !it.done }
            TaskFilter.COMPLETADAS -> state.task.filter { it.done }
        }
        val visible = if (state.sortByPriority){
            filtered.sortedBy { it.priority.ordinal }
        }else{
            filtered
        }
        val completed = state.task.count { it.done }
        val percent = if (state.task.isEmpty()) 0 else completed * 100 / state.task.size
        return state.copy(
            visibleTask = visible,
            pendingCount = state.task.size - completed,
            completedCount = completed,
            progressPercent = percent
        )
    }
    private fun mockTasks (): List<Task> = listOf(
        Task(id=1, title = "Estudiar para examen de calculo -miercoles", priority = Priority.ALTA),
        Task(id=2, title = "Repasar temas de programacion", priority = Priority.MEDIA),
        Task(id=3, title = "Ir de compras el sabado", priority = Priority.BAJA),
    )
}

fun onAddTask(title: String){
    val cleanTitle = title.trim()
    if (cleanTitle.isEmpty()){
        val _uiState = null
        _uiState.update { it.copy(showError = true)}
        return
    }
    _uiState.update { current ->
        val newId = (current.tasks.maxOfOrNull {it.id} ?: 0) +1
        val newTask = Task(id = newId, title = cleanTitle, priority = current.selectedPriority)
        recalculate(current.copy(tasks=current.tasks + newTask, showError = false))
    }
}

fun recalculate(copy: Any) {}

fun onTitleChanged(){
    _uiState.update { it.copy(showError = false)}
}

fun onToggleTask(id: Int){
    _uiState.update { current ->
        val updated = current.task.map { task ->
            if ( task.id == id) task.copy(done = !task.done) else task
        }
        recalculate(current.copy(task = updated))
    }
}

fun onDeleteTask(id: Int){
    _uiState.update { current ->
        recalculate(current.copy(task = current.task.filter { it.id != id}))
    }
}

fun onClearCompleted(){
    _uiState.update { current ->
        recalculate(current.copy(tasks = current.task.filter {!it.done}))
    }
}

fun onFilterSelected(filter: TaskFilter){
    _uiState.update {
        recalculate(it.copy(filter = filter))
    }
}

fun onShortChanged(enabled: Boolean){
    _uiState.update {
        recalculate(it.copy(sortByPriority = enabled))
    }
}

fun onPrioritySelected(priority: Priority){
    _uiState.update { it.copy(selectedPriority = priority) }
}