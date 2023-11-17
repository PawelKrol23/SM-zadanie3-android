package pl.edu.pb.todoapp2

object TaskStorage {
    val tasks: MutableList<Task> = mutableListOf()

    init {
        val taskNumber = 200
        for(i in 1..taskNumber) {
            val category = if (i%3 == 0) Category.STUDIES else Category.HOME
            tasks.add(Task("Bardzo ważne zadanko #$i", i % 2 == 0, category))
        }
    }

    fun addTask(task: Task) = tasks.add(task)
}
