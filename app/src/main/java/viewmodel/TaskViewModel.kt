package viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import model.Priority
import model.Task
import model.TaskFilter

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
) {
    val visibleTasks: Any
}

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

    fun onTitleChanged() {
        TODO("Not yet implemented")
    }

    fun onAddTask(text: String) {}
}