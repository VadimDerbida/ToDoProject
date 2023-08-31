package com.example.todoproject.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoproject.R
import com.example.todoproject.domain.model.Status
import com.example.todoproject.domain.model.Task
import com.google.android.material.textview.MaterialTextView

class TaskAdapter(
    private val onClickListener: OnClickListener,
    private val onLongClickListener: OnLongClickListener,
): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var list: List<Task> = listOf()

    fun setTasks(tempTasks: List<Task>) {
        list = tempTasks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_task_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener.onClick(list[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onClick(list[position])
            true
        }
        return holder.bind(list[position])
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val title = itemView.findViewById<MaterialTextView>(R.id.title_text)
            val subject = itemView.findViewById<MaterialTextView>(R.id.subject_text)
            val date = itemView.findViewById<TextView>(R.id.date)
            val status = itemView.findViewById<TextView>(R.id.status_bar)

            title.text = task.title
            subject.text = task.subject
            date.text = task.date
            status.text = task.status
            status.backgroundTintList = when (task.status) {
                "Canceled" -> ColorStateList.valueOf(Color.RED)
                "Done" -> ColorStateList.valueOf(Color.GREEN)
                else -> ColorStateList.valueOf(Color.BLUE)
            }
        }

        private fun edit(status: Status){
            val statusBar = itemView.findViewById<TextView>(R.id.status_bar)
            statusBar.text = Status.CANCEL.toString()
        }
    }


}

class OnClickListener(val clickListener: (todo: Task) -> Unit) {
    fun onClick(todo: Task) = clickListener(todo)
}

class OnLongClickListener(val clickListener: (todo: Task) -> Unit) {
    fun onClick(todo: Task) = clickListener(todo)
}




