package com.example.todoproject.data.contract

import androidx.lifecycle.LiveData
import com.example.todoproject.core.Resource
import com.example.todoproject.domain.model.Task
// all necessary functions for interaction with task in RealTime DB. For using further in different places without copy past
interface FirebaseRealtimeInterface {

    fun getAllTodos(): LiveData<Resource<List<Task>>>

    fun deleteTodo(task: Task): LiveData<Resource<String>>

    fun addTodo(task: Task): LiveData<Resource<String>>

    fun editTodo(task: Task): LiveData<Resource<String>>

    fun updateTodo(status: String, taskId: String): LiveData<Resource<String>>

}