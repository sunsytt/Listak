package com.siyu.task.model

enum class Priority { ALTA, MEDIA, BAJA }

enum class TaskFilter { TODAS, PENDIENTES, COMPLETADAS }


data class Task(
    val id: Int,
    val title: String,
    val priority: Priority,
    val done: Boolean = false
)