package com.siyu.task.ui.components

import com.siyu.task.R
import com.siyu.task.model.Category
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

val Category.labelRes: Int
    get() = when (this) {
        Category.ESTUDIOS -> R.string.category_studies
        Category.PERSONAL -> R.string.category_personal
        Category.TRABAJO -> R.string.category_work
        Category.HOGAR -> R.string.category_home
    }
