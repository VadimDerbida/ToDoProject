package com.example.todoproject.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.databinding.FragmentDashboardBinding
import com.example.todoproject.domain.model.Task
import com.example.todoproject.presentation.*
import com.example.todoproject.presentation.dialogs.ChangeStatusDialog
import com.example.todoproject.presentation.dialogs.NewTaskDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DashboardFragment : Fragment(), NewTaskDialog.DialogNextButtonClickListener,
    ChangeStatusDialog.DialogNextButtonClickListener {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private var _adapter: TaskAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel by viewModels<DashboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeData()
        registerEvents()
        swipeToDeleteCallback()
    }

    private fun observeData() {
        // SUCCESS
        viewModel.taskLiveData.observe(viewLifecycleOwner) { tasks ->
            binding.noDataPlaceholder.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
            adapter.setTasks(tasks)
        }

        // ERROR
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        // LOADING
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.loadingBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun registerEvents() {
        binding.addTaskButton.setOnClickListener {
            val newTaskDialog = NewTaskDialog(task = null)
            newTaskDialog.setTargetFragment(this, 0)
            newTaskDialog.show(parentFragmentManager, "NewTaskScreen")
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        _adapter = TaskAdapter(
            onClickListener = OnClickListener {
                // edit feature
                val newTaskDialog = NewTaskDialog(task = it)
                newTaskDialog.setTargetFragment(this, 0)
                newTaskDialog.show(parentFragmentManager, "NewTaskScreen")
            },
            onLongClickListener = OnLongClickListener {
                val changeStatusDialog = ChangeStatusDialog(it.id)
                changeStatusDialog.setTargetFragment(this, 0)
                changeStatusDialog.show(parentFragmentManager, "Change status screen")
            }
        )
        binding.recyclerView.adapter = adapter
    }

    override fun onSaveTask(task: Task, isEdit: Boolean) {
        if (isEdit) {
            viewModel.editTask(task)
        } else {
            viewModel.addTask(task = task)
        }
    }

    private fun swipeToDeleteCallback() {
        val swipeToDelete = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.list[position]
                viewModel.deleteTask(task)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onSaveStatus(status: String, taskId: String) {
        // call firebase to update specific field
        viewModel.updateTaskStatus(status, taskId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _adapter = null
    }
}
