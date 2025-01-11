package com.example.concertio.data.users

import androidx.lifecycle.LiveData
import com.example.concertio.room.DatabaseHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepository {
    private val usersDao = DatabaseHolder.getDatabase().usersDao()

    suspend fun insertUsers(vararg users: UserModel) = withContext(Dispatchers.IO) {
        usersDao.insertAll(*users)
    }

    suspend fun upsertUser(user: UserModel) = withContext(Dispatchers.IO) {
        usersDao.upsert(user)
    }

    suspend fun deleteAllUsers() = withContext(Dispatchers.IO) {
        usersDao.deleteAll()
    }

    suspend fun deleteByUid(uid: String) = withContext(Dispatchers.IO) {
        usersDao.deleteByUid(uid)
    }

    fun getUserByUid(uid: String): LiveData<UserModel> {
        return usersDao.getUserByUid(uid)
    }
}