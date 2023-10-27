package pl.edu.pb.todoapp2

import androidx.fragment.app.Fragment
import java.util.UUID

class MainActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        val taskId = intent.getSerializableExtra(KEY_EXTRA_TASK_ID) as UUID
        return TaskFragment.newInstance(taskId)
    }
}