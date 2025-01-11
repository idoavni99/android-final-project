package com.example.concertio.data.users

import com.example.concertio.room.DatabaseHolder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UsersRepository {
    private val usersDao = DatabaseHolder.getDatabase().usersDao()
    private val firestoreHandle = Firebase.firestore.collection("users")

    suspend fun insertUsers(vararg users: UserModel) = withContext(Dispatchers.IO) {
        firestoreHandle.add(users).await()
        usersDao.upsertAll(*users)
    }

    suspend fun upsertUser(user: UserModel) = withContext(Dispatchers.IO) {
        firestoreHandle.document(user.uid).set(user.toRemoteSourceUser()).await()
        usersDao.upsertAll(user)
    }

    suspend fun deleteAllUsers() = withContext(Dispatchers.IO) {
        usersDao.deleteAll()
    }

    suspend fun deleteById(uid: String) = withContext(Dispatchers.IO) {
        usersDao.deleteByUid(uid)
    }

    suspend fun cacheUserIfNotExisting(uid: String) = withContext(Dispatchers.IO) {
        val cachedResult = usersDao.getUserByUid(uid)
        if (cachedResult == null) {
            this@UsersRepository.getUserFromRemoteSource(uid)
        }
    }

    suspend fun cacheUsersIfNotExisting(uids: List<String>) = withContext(Dispatchers.IO) {
        val existingUserIds = usersDao.getExistingUserIds(uids)
        val nonExistingUserIds = uids.filter { !existingUserIds.contains(it) }
        this@UsersRepository.getUsersFromRemoteSource(nonExistingUserIds)
    }

    suspend fun getUserByUid(uid: String): UserModel? = withContext(Dispatchers.IO) {
        val cachedResult = usersDao.getUserByUid(uid)
        if (cachedResult != null) return@withContext cachedResult

        return@withContext this@UsersRepository.getUserFromRemoteSource(uid)
    }

    private suspend fun getUserFromRemoteSource(uid: String): UserModel? =
        withContext(Dispatchers.IO) {
            val user =
                firestoreHandle.document(uid).get().await().toObject(RemoteSourceUser::class.java)
                    ?.toUserModel()
            if (user != null) {
                usersDao.upsertAll(user)
            }
            return@withContext user
        }

    private suspend fun getUsersFromRemoteSource(uids: List<String>): List<UserModel> =
        withContext(Dispatchers.IO) {
            val usersQuery =
                if (uids.isNotEmpty()) firestoreHandle.whereIn("uid", uids) else firestoreHandle
            val users = usersQuery.get().await().toObjects(RemoteSourceUser::class.java)
                .map { it.toUserModel() }
            if (users.isNotEmpty()) {
                usersDao.upsertAll(*users.toTypedArray())
            }
            return@withContext users
        }
}