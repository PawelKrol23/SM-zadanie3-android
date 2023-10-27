package pl.edu.pb.todoapp2

import androidx.fragment.app.Fragment

class TaskListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return TaskListFragment()
    }
}