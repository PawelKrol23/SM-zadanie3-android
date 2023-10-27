package pl.edu.pb.todoapp2

import java.util.Date
import java.util.UUID

data class Task(var name: String, var done: Boolean) {
    val id = UUID.randomUUID()
    val date = Date()
}