package com.example.todoproject.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.todoproject.databinding.StatusChangerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeStatusDialog(private val taskId: String): DialogFragment() {

    private var _binding: StatusChangerBinding? = null
    private val binding get() = _binding!!

    interface DialogNextButtonClickListener {
        fun onSaveStatus(status: String, taskId: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StatusChangerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        statusClicked()
    }

    private fun statusClicked(){
        val listener = targetFragment as DialogNextButtonClickListener?
        binding.cancelButton.setOnClickListener{
            listener?.onSaveStatus(status = "Canceled", taskId = taskId)
            dismiss()
        }

        binding.doneButton.setOnClickListener {
            listener?.onSaveStatus(status = "Done", taskId = taskId)
            dismiss()
        }
    }
}