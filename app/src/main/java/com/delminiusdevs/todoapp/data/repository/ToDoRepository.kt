package com.delminiusdevs.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.delminiusdevs.todoapp.data.ToDoDao
import com.delminiusdevs.todoapp.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    //get the reference from our database
    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData) {
        toDoDao.deleteItem(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }
}