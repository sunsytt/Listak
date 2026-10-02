package model

import androidx.annotation.StringRes
import com.siyu.task.R

enum class Priority(@StringRes val labelRes: Int){
    ALTA(R.string.priority_high),
    MEDIA(R.string.priority_medium),
    BAJA(R.string.priority_low)
}
enum class TaskFilter(@StringRes val labelRes:Int){
    TODAS(R.string.filter_all),
    PENDIENTES(R.string.filter_pending),
    COMPLETADAS(R.string.filter_completed)
}

data class Task(
    val id: Int,
    val title: String,
    val priority: Priority,
    val done: Boolean = false
)