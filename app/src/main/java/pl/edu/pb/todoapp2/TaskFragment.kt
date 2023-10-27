package pl.edu.pb.todoapp2

import android.icu.text.MessagePattern.ArgType
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.UUID

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var nameField: EditText
    private lateinit var dateButton: Button
    private lateinit var doneCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskId = requireArguments().getSerializable(ARG_TASK_ID) as UUID
        task = TaskStorage.getInstance().tasks.first { it.id.equals(taskId) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)

        nameField = view.findViewById(R.id.task_name)
        nameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                task.name = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        dateButton = view.findViewById(R.id.task_date)
        dateButton.text = task.date.toString()
        dateButton.isEnabled = false;

        doneCheckBox = view.findViewById(R.id.task_done)
        doneCheckBox.isChecked = task.done
        doneCheckBox.setOnCheckedChangeListener { _, isChecked -> task.done = isChecked }

        return view
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