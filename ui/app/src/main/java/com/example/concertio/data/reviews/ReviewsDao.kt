package com.example.concertio.data.reviews

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface ReviewsDao {
    @Query("SELECT * FROM reviews LIMIT :limit OFFSET :offset")
    fun getAllPaginated(limit: Int, offset: Int): LiveData<List<ReviewWithReviewer>>

    @Query("SELECT * FROM reviews WHERE id = :id")
    fun findById(id: String): LiveData<ReviewWithReviewer?>

    @Upsert
    fun upsertAll(vararg review: ReviewModel)

    @Delete
    fun delete(review: ReviewModel)

    @Query("DELETE FROM reviews WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM reviews")
    fun deleteAll()

    @Update
    fun update(review: ReviewModel)
}