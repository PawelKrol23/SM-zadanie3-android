package pl.edu.pb.todoapp2

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var nameField: EditText
    private lateinit var dateField: EditText
    private lateinit var doneCheckBox: CheckBox
    private lateinit var categorySpinner: Spinner

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val taskId = requireArguments().getSerializable(ARG_TASK_ID) as UUID
        task = TaskStorage.tasks.first { it.id.equals(taskId) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        nameField = view.findViewById(R.id.task_name)
        nameField.setText(task.name)
        nameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                task.name = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        dateField = view.findViewById(R.id.task_date)

        val date = DatePickerDialog.OnDateSetListener { view12, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            setupDateFieldValue(calendar.time)
            task.date = calendar.time
        }
        dateField.setOnClickListener { view ->
            DatePickerDialog(
                requireContext(),
                date,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }
        dateField.setText(task.date.toString())

        doneCheckBox = view.findViewById(R.id.task_done)
        doneCheckBox.isChecked = task.done
        doneCheckBox.setOnCheckedChangeListener { _, isChecked -> task.done = isChecked }

        categorySpinner = view.findViewById(R.id.task_category)
        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Category.values())
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                task.category = Category.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        categorySpinner.setSelection(task.category.ordinal)

        return view
    }

    fun setupDateFieldValue(date: Date) {
        val locale = Locale("pl", "PL")
        val dateFormat = SimpleDateFormat("dd.MM.yyy", locale)
        dateField.setText(dateFormat.format(date))
    }

    companion object {
        fun newInstance(taskId: UUID): TaskFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARG_TASK_ID, taskId)
            val taskFragment = TaskFragment()
            taskFragment.arguments = bundle
            return taskFragment
        }
    }
}

const val ARG_TASK_ID = "task_id"