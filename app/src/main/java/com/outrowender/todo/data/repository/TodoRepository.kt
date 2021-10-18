package com.outrowender.todo.data.repository

import androidx.lifecycle.LiveData
import com.outrowender.todo.data.TodoDao
import com.outrowender.todo.data.models.TodoData

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<TodoData>> = todoDao.getAllData()
    val sortByHighPriority: LiveData<List<TodoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TodoData>> = todoDao.sortByLowPriority()

    suspend fun insertData(toDoData: TodoData){
        todoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: TodoData){
        todoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: TodoData){
        todoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll(){
        todoDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TodoData>> {
        return todoDao.searchDatabase(searchQuery)
    }

}