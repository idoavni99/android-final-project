package com.example.concertio.data.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface UsersDao {
    @Query("SELECT * FROM users WHERE uid LIKE :uid LIMIT 1")
    suspend fun getUserByUid(uid: String): UserModel?

    @Query("SELECT uid FROM users WHERE uid IN (:uids)")
    suspend fun getExistingUserIds(uids: List<String>): List<String>

    @Insert
    fun insertAll(vararg users: UserModel)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("DELETE FROM users WHERE uid = :uid")
    fun deleteByUid(uid: String)

    @Update
    fun updateUserData(user: UserModel)

    @Upsert
    fun upsert(user: UserModel)
}