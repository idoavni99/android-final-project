package com.example.concertio.data.reviews

import androidx.lifecycle.LiveData
import com.example.concertio.room.DatabaseHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewsRepository() {
    private val reviewsDao = DatabaseHolder.getDatabase().reviewsDao()

    suspend fun addStudent(student: ReviewModel) = withContext(Dispatchers.IO) {
        reviewsDao.insertAll(student)
    }

    suspend fun editStudent(student: ReviewModel) = withContext(Dispatchers.IO) {
        reviewsDao.update(student)
    }

    suspend fun deleteStudentByUid(uid: String) = withContext(Dispatchers.IO) {
        reviewsDao.deleteByUid(uid)
    }

    fun getStudentByUid(uid: String) = reviewsDao.findByUid(uid)

    fun getStudentsList(): LiveData<List<ReviewWithReviewer>> = reviewsDao.getAll()
}