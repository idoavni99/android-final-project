package com.example.concertio.data.reviews

import androidx.lifecycle.LiveData
import com.example.concertio.data.users.UsersRepository
import com.example.concertio.room.DatabaseHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewsRepository() {
    private val reviewsDao = DatabaseHolder.getDatabase().reviewsDao()
    private val usersRepository = UsersRepository()
    private val firestoreHandle = Firebase.firestore.collection("reviews")

    suspend fun addReview(vararg reviews: ReviewModel) = withContext(Dispatchers.IO) {
        val batchHandle = Firebase.firestore.batch()
        reviews.forEach {
            batchHandle.set(firestoreHandle.document(it.uid), it.toRemoteSource())
        }
        batchHandle.commit().await()

        reviewsDao.insertAll(*reviews)
    }

    suspend fun editReview(review: ReviewModel) = withContext(Dispatchers.IO) {
        firestoreHandle.document(review.uid).set(review.toRemoteSource()).await()
        reviewsDao.update(review)
    }

    suspend fun deleteReviewByUid(uid: String) = withContext(Dispatchers.IO) {
        firestoreHandle.document(uid).delete().await()
        reviewsDao.deleteByUid(uid)
    }

    fun getReviewByUid(uid: String, scope: CoroutineScope): LiveData<ReviewWithReviewer?> {
        return reviewsDao.findByUid(uid)
    }

    fun getReviewsList(
        limit: Int,
        offset: Int,
        scope: CoroutineScope
    ): LiveData<List<ReviewWithReviewer>> {
        return reviewsDao.getAllPaginated(limit, offset)
    }


    suspend fun loadReviewFromRemoteSource(uid: String) = withContext(Dispatchers.IO) {
        val review =
            firestoreHandle.document(uid).get().await().toObject(RemoteSourceReview::class.java)
                ?.toReviewModel()
        if (review != null) {
            reviewsDao.insertAll(review)
            usersRepository.cacheUserIfNotExisting(review.reviewerUid)
        }
        return@withContext reviewsDao.findByUid(uid)
    }

    suspend fun loadReviewsFromRemoteSource(limit: Int, offset: Int) =
        withContext(Dispatchers.IO) {
            val reviews = firestoreHandle.orderBy("review").startAt(offset).limit(limit.toLong())
                .get().await().toObjects(RemoteSourceReview::class.java).map { it.toReviewModel() }
            if (reviews.isNotEmpty()) {
                reviewsDao.insertAll(*reviews.toTypedArray())
                usersRepository.cacheUsersIfNotExisting(reviews.map { it.reviewerUid })
            }
        }
}