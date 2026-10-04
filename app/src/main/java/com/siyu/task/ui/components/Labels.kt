package com.siyu.task.ui.components

import com.siyu.task.R
import com.siyu.task.model.Priority
import com.siyu.task.model.TaskFilter


val Priority.labelRes: Int
    get() = when (this) {
        Priority.ALTA -> R.string.priority_high
        Priority.MEDIA -> R.string.priority_medium
        Priority.BAJA -> R.string.priority_low
    }

val TaskFilter.labelRes: Int
    get() = when (this) {
        TaskFilter.TODAS -> R.string.filter_all
        TaskFilter.PENDIENTES -> R.string.filter_pending
        TaskFilter.COMPLETADAS -> R.string.filter_completed
    }
