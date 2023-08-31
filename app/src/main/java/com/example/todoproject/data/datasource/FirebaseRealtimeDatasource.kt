package com.example.todoproject.data.datasource

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoproject.core.Resource
import com.example.todoproject.data.contract.FirebaseRealtimeInterface
import com.example.todoproject.domain.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

// Setting logic for interaction with RealTime DB

class FirebaseRealtimeDatasource @Inject constructor(private val auth: FirebaseAuth): FirebaseRealtimeInterface {

    override fun getAllTodos(): LiveData<Resource<List<Task>>> {
        val resultLiveData = MutableLiveData<Resource<List<Task>>>()
        resultLiveData.postValue(Resource.loading())
        val tasksReferences = FirebaseDatabase.getInstance().getReference("Tasks/${auth.uid}")
        tasksReferences.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasksArray = arrayListOf<Task>()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        tasksArray.add(task)
                    }
                }
                resultLiveData.postValue(Resource.success(tasksArray))
            }

            override fun onCancelled(error: DatabaseError) {
                resultLiveData.postValue(Resource.error(error.toException()))
            }
        })

        return resultLiveData
    }

    override fun deleteTodo(task: Task): LiveData<Resource<String>>  {
        val resultLiveData = MutableLiveData<Resource<String>>()
        resultLiveData.postValue(Resource.loading())
        val uid = auth.uid

        val tasksReferences = FirebaseDatabase.getInstance().getReference("Tasks/$uid/${task.id}")
        tasksReferences.child("").removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                resultLiveData.postValue(Resource.success("Success"))
            } else {
                resultLiveData.postValue(Resource.error(it.exception!!))
            }
        }
        return resultLiveData
    }

    override fun addTodo(task: Task): LiveData<Resource<String>> {
        val resultLiveData = MutableLiveData<Resource<String>>()
        resultLiveData.postValue(Resource.loading())

        val uid = auth.uid
        val tasksReferences = FirebaseDatabase.getInstance().getReference("Tasks/$uid/")
        tasksReferences.child("/${task.id}").setValue(task).addOnCompleteListener {
            if (it.isSuccessful) {
                resultLiveData.postValue(Resource.success("Success"))
            } else {
                resultLiveData.postValue(Resource.error(it.exception!!))
            }
        }
        return resultLiveData
    }

    override fun editTodo(task: Task): LiveData<Resource<String>> {
        val resultLiveData = MutableLiveData<Resource<String>>()
        resultLiveData.postValue(Resource.loading())

        val titleMap = mapOf(Pair("title", task.title))
        val subjectMap = mapOf(Pair("subject", task.subject))
        val dateMap = mapOf(Pair("date", task.date))

        val uid = auth.uid
        val tasksReferences = FirebaseDatabase.getInstance().getReference("Tasks/$uid/${task.id}")
        tasksReferences.child("").updateChildren(titleMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                resultLiveData.postValue(Resource.error(it))
            }

        tasksReferences.child("").updateChildren(subjectMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                resultLiveData.postValue(Resource.error(it))
            }

        tasksReferences.child("").updateChildren(dateMap)
            .addOnSuccessListener {
                resultLiveData.postValue(Resource.success("Success"))
            }
            .addOnFailureListener {
                resultLiveData.postValue(Resource.error(it))
            }

        return resultLiveData
    }

    override fun updateTodo(status: String, taskId: String): LiveData<Resource<String>> {
        val resultLiveData = MutableLiveData<Resource<String>>()
        resultLiveData.postValue(Resource.loading())

        val statusMap = mapOf(Pair("status", status))
        val uid = auth.uid
        val tasksReferences = FirebaseDatabase.getInstance().getReference("Tasks/$uid/${taskId}")
        tasksReferences.child("").updateChildren(statusMap)
            .addOnSuccessListener {
                resultLiveData.postValue(Resource.success("Success"))
            }
            .addOnFailureListener {
                resultLiveData.postValue(Resource.error(it))
            }
        return resultLiveData
    }
}