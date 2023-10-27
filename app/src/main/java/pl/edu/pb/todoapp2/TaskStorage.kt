package pl.edu.pb.todoapp2

class TaskStorage private constructor(){
    val tasks: MutableList<Task> = mutableListOf()

    companion object {
        private val instance: TaskStorage = TaskStorage()

        fun getInstance() = instance
    }

    init {
        val taskNumber = 200
        for(i in 1..taskNumber) {
            tasks.add(Task("Bardzo ważne zadanko #$i", i % 2 == 0))
        }
    }
}
