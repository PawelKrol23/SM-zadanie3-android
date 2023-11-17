package pl.edu.pb.todoapp2

import java.util.Date
import java.util.UUID

data class Task(var name: String, var done: Boolean, var category: Category) {
    val id = UUID.randomUUID()
    var date = Date()
}