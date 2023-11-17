package pl.edu.pb.todoapp2

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TaskListFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: TaskAdapter? = null
    private var subtitleVisible = false

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

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_menu, menu)
        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task(name = "", done = false, category = Category.HOME)
                TaskStorage.addTask(task)
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra(KEY_EXTRA_TASK_ID, task.id)
                startActivity(intent)
                true
            }

            R.id.show_subtitle -> {
                subtitleVisible = !subtitleVisible
                requireActivity().invalidateOptionsMenu()
                updateSubtitle()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSubtitle() {
        val taskCount = TaskStorage.tasks.filter { !it.done }.size
        var subtitle: String? = getString(R.string.subtitle_format, taskCount)
        if (!subtitleVisible) {
            subtitle = null
        }
        (activity as AppCompatActivity).supportActionBar!!.subtitle = subtitle
    }

    private inner class TaskHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_task, parent, false)), View.OnClickListener {

        private val nameTextView: TextView = itemView.findViewById(R.id.task_item_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.task_item_date)
        private val iconImageView: ImageView = itemView.findViewById(R.id.task_item_icon)
        val checkBox: CheckBox = itemView.findViewById(R.id.task_item_done)
        private lateinit var task: Task

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task
            var nameToSet = task.name
            if (nameToSet.length > 30) {
                nameToSet = nameToSet.substring(0, 30) + "..."
            }
            nameTextView.text = nameToSet
            crossTextView(task.done)
            dateTextView.text = task.date.toString()
            if (task.category == Category.HOME) {
                iconImageView.setImageResource(R.drawable.ic_house)
            } else {
                iconImageView.setImageResource(R.drawable.ic_book)
            }
            checkBox.isChecked = task.done
        }

        fun crossTextView(done: Boolean) {
            if(done) {
                nameTextView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                nameTextView.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
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

            val checkBox = holder.checkBox
            checkBox.isChecked = tasks[position].done
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                tasks[holder.bindingAdapterPosition].done = isChecked
                holder.crossTextView(isChecked)
            }
        }
    }

    private fun updateView() {
        val tasks = TaskStorage.tasks

        if (adapter == null) {
            adapter = TaskAdapter(tasks)
            recyclerView.adapter = adapter
        } else {
            adapter!!.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    override fun onResume() {
        super.onResume()
        updateView()
    }
}

const val KEY_EXTRA_TASK_ID = "extra_task_id"
const val KEY_SUBTITLE_VISIBLE = "subtitle_visible"