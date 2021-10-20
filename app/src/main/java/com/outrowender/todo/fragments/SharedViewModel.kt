package com.outrowender.todo.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.outrowender.todo.R
import com.outrowender.todo.data.models.Priority
import com.outrowender.todo.data.models.TodoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    fun parsePriority(priority: String): Priority {
        return when(priority){
            "High" -> {Priority.HIGH}
            "Medium" -> {Priority.MEDIUM}
            "Low" -> {Priority.LOW}
            else -> Priority.MEDIUM
        }
    }

    fun parsePriority(priority: Priority): Int {
        return when(priority){
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    fun validate(mTitle: String, mPriority: String, mDescription: String): Boolean {
        //TODO: TextUtils validation and limit description to 300 chars

        return true
    }

    fun checkIfDatabaseEmpty(toDoData: List<TodoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {}
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when(position){
                0 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green)) }
            }
        }
    }

}