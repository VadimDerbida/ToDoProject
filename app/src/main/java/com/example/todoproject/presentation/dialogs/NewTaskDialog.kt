package com.example.todoproject.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todoproject.databinding.NewTaskScreenBinding
import com.example.todoproject.domain.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskDialog(private val task: Task?) : DialogFragment() {
    private var _binding: NewTaskScreenBinding? = null
    private val binding get() = _binding!!

    private var startDate: String = ""

    interface DialogNextButtonClickListener {
        fun onSaveTask(task: Task, isEdit: Boolean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        initData()
        registerEvents()
        setupDatePicker()
    }

    private fun initData() {
        if (task != null) {
            binding.title.setText(task.title ?: "")
            binding.subjectText.setText(task.subject ?: "")
            startDate = task.date ?: ""
        }
    }

    private fun convertLongToString(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        return format.format(date)
    }

    private fun setupDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()
        binding.datePickerButton.setOnClickListener {
            datePicker.show(parentFragmentManager, "date")
            datePicker.addOnPositiveButtonClickListener {
                startDate = convertLongToString(it)
            }
        }
    }


    private fun registerEvents() {
        binding.doneTaskButton.setOnClickListener {
            if (task != null) {
                val title = binding.title.text.toString()
                val subject = binding.subjectText.text.toString()
                var date = startDate
                val status = task.status ?: "Do it"
                val id = task.id
                if (date == "") date = org.joda.time.LocalDate.now().toString("dd.MM.yy")

                val task = Task(title, subject, id, date, status)

                if (title.isNotEmpty() && subject.isNotEmpty()) {
                    val listener = targetFragment as DialogNextButtonClickListener?
                    listener?.onSaveTask(task, isEdit = true)
                    dismiss()
                } else {
                    Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
                }
            } else {
                val toDoTask = binding.title.text.toString()
                val subject = binding.subjectText.text.toString()
                var date = startDate
                val id = UUID.randomUUID().toString()
                val status = "Do it"
                if (date == "") date = org.joda.time.LocalDate.now().toString("dd.MM.yy")

                val task = Task(toDoTask, subject, id, date, status)

                if (toDoTask.isNotEmpty() && subject.isNotEmpty()) {
                    val listener = targetFragment as DialogNextButtonClickListener?
                    listener?.onSaveTask(task, isEdit = false)
                    dismiss()
                } else {
                    Toast.makeText(context, "Please type some task", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = NewTaskScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

}


