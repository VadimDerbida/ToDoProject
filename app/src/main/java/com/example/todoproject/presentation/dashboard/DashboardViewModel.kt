package com.example.todoproject.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoproject.core.Resource
import com.example.todoproject.data.contract.FirebaseRealtimeInterface
import com.example.todoproject.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val firebaseRealtimeInterface: FirebaseRealtimeInterface): ViewModel() {

    private val _tasksLiveData = MutableLiveData<List<Task>>()
    val taskLiveData: LiveData<List<Task>> get() = _tasksLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData

    init {
        loadTasks()
    }

    private fun loadTasks() {
        firebaseRealtimeInterface.getAllTodos().observeForever { result ->
            when (result.status) {
                Resource.Status.LOADING -> _loadingLiveData.postValue(true)
                Resource.Status.SUCCESS -> {
                    _loadingLiveData.postValue(false)
                    if (result.data != null) {
                        _tasksLiveData.postValue(result.data)
                    }
                }
                Resource.Status.ERROR -> {
                    _loadingLiveData.postValue(false)
                    _errorLiveData.postValue(result.exception?.message ?: "Unknown error")
                }
            }
        }
    }

    fun addTask(task: Task) {
        firebaseRealtimeInterface.addTodo(task).observeForever { result ->
            when (result.status) {
                Resource.Status.LOADING -> _loadingLiveData.postValue(true)
                Resource.Status.SUCCESS -> {
                    _loadingLiveData.postValue(false)
                }
                Resource.Status.ERROR -> {
                    _loadingLiveData.postValue(false)
                    _errorLiveData.postValue(result.exception?.message ?: "Unknown error")
                }
            }
        }
    }

    fun editTask(task: Task) {
        firebaseRealtimeInterface.editTodo(task).observeForever { result ->
            when (result.status) {
                Resource.Status.LOADING -> _loadingLiveData.postValue(true)
                Resource.Status.SUCCESS -> {
                    _loadingLiveData.postValue(false)
                }
                Resource.Status.ERROR -> {
                    _loadingLiveData.postValue(false)
                    _errorLiveData.postValue(result.exception?.message ?: "Unknown error")
                }
            }
        }
    }

    fun deleteTask(task: Task) {
        firebaseRealtimeInterface.deleteTodo(task).observeForever { result ->
            when (result.status) {
                Resource.Status.LOADING -> _loadingLiveData.postValue(true)
                Resource.Status.SUCCESS -> {
                    _loadingLiveData.postValue(false)
                }
                Resource.Status.ERROR -> {
                    _loadingLiveData.postValue(false)
                    _errorLiveData.postValue(result.exception?.message ?: "Unknown error")
                }
            }
        }
    }

    fun updateTaskStatus(status: String, taskId: String) {
        firebaseRealtimeInterface.updateTodo(taskId = taskId, status = status).observeForever { result ->
            when (result.status) {
                Resource.Status.LOADING -> _loadingLiveData.postValue(true)
                Resource.Status.SUCCESS -> {
                    _loadingLiveData.postValue(false)
                }
                Resource.Status.ERROR -> {
                    _loadingLiveData.postValue(false)
                    _errorLiveData.postValue(result.exception?.message ?: "Unknown error")
                }
            }
        }
    }

}