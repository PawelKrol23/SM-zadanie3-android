package pl.edu.pb.todoapp2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
        recyclerView = view.findViewById(R.id.task_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        updateView()
        return view
    }

    private inner class TaskHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_task, parent, false)), View.OnClickListener {

        private val nameTextView: TextView = itemView.findViewById(R.id.task_item_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.task_item_date)
        private lateinit var task: Task

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            nameTextView.text = task.name
            dateTextView.text = task.date.toString()
        }

        override fun onClick(p0: View?) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra(KEY_EXTRA_TASK_ID, task.id)
            startActivity(intent)
        }
    }

    private inner class TaskAdapter(val tasks: MutableList<Task>)
        : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return TaskHolder(layoutInflater, parent)
        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }
    }

    private fun updateView() {
        val taskStorage = TaskStorage.getInstance()
        val tasks = taskStorage.tasks

        if (adapter == null) {
            adapter = TaskAdapter(tasks)
            recyclerView.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }
}

const val KEY_EXTRA_TASK_ID = "extra_task_id"