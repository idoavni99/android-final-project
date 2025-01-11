package com.example.concertio.data.students

import androidx.lifecycle.LiveData
import com.example.concertio.room.DatabaseHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StudentsRepository() {
    private val studentsDao = DatabaseHolder.getDatabase().studentsDao()

    suspend fun addStudent(student: StudentModel) = withContext(Dispatchers.IO) {
        studentsDao.insertAll(student)
    }

    suspend fun editStudent(student: StudentModel) = withContext(Dispatchers.IO) {
        studentsDao.update(student)
    }

    suspend fun deleteStudentByUid(uid: String) = withContext(Dispatchers.IO) {
        studentsDao.deleteByUid(uid)
    }

    fun getStudentByUid(uid: String) = studentsDao.findByUid(uid)

    fun getStudentsList(): LiveData<List<StudentModel>> = studentsDao.getAll()
}