package com.example.concertio.data.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.concertio.http.RetrofitHolder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey val uid: String = "",
    @ColumnInfo("name") val name: String,
    @ColumnInfo("email") val email: String?,
    @ColumnInfo("profilePicture") val profilePicture: String? = null
) {
    fun toRemoteSourceUser(): RemoteSourceUser {
        return RemoteSourceUser(
            uid = uid,
            name = name,
            email = email,
            profilePicture = profilePicture
        )
    }

    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        suspend fun fromFirebaseAuth(): UserModel = withContext(Dispatchers.IO) {
            val user = FirebaseAuth.getInstance().currentUser
            val profilePhotoBase64 = user?.photoUrl?.let {
                Base64.encode(URL(it.toString()).readBytes())
            }
            return@withContext UserModel(
                uid = user?.uid!!,
                email = user.email!!,
                name = user.displayName!!,
                profilePicture = profilePhotoBase64
            )
        }
    }
}
