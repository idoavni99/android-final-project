package com.example.concertio.data.reviews

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReviewsDao {
    @Query("SELECT * FROM reviews")
    fun getAll(): LiveData<List<ReviewWithReviewer>>

    @Query("SELECT * FROM reviews WHERE uid IN (:reviewIds)")
    fun loadAllByIds(reviewIds: IntArray): LiveData<List<ReviewWithReviewer>>

    @Query("SELECT * FROM reviews WHERE uid LIKE :uid LIMIT 1")
    fun findByUid(uid: String): LiveData<ReviewWithReviewer?>

    @Insert
    fun insertAll(vararg review: ReviewModel)

    @Delete
    fun delete(review: ReviewModel)

    @Query("DELETE FROM reviews WHERE uid = :uid")
    fun deleteByUid(uid: String)

    @Query("DELETE FROM reviews")
    fun deleteAll()

    @Update
    fun update(review: ReviewModel)
}