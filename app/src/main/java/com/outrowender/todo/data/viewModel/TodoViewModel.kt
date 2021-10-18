package com.outrowender.todo.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.outrowender.todo.data.TodoDatabase
import com.outrowender.todo.data.models.TodoData
import com.outrowender.todo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    private var repository: TodoRepository = TodoRepository(todoDao)

    val getAllData: LiveData<List<TodoData>> = repository.getAllData
    val sortByHighPriority: LiveData<List<TodoData>> = repository.sortByHighPriority
    val sortByLowPriority: LiveData<List<TodoData>> = repository.sortByLowPriority

    fun insertData(toDoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TodoData>>{
        return repository.searchDatabase(searchQuery)
    }

}