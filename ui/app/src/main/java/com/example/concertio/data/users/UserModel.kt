package com.example.concertio.data.users

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey val uid: String = "",
    val name: String,
    val email: String?,
) {
    fun toRemoteSourceUser(): RemoteSourceUser {
        return RemoteSourceUser(
            uid = uid,
            name = name,
            email = email
        )
    }

    companion object {
        fun fromFirebaseAuth(): UserModel {
            val user = FirebaseAuth.getInstance().currentUser

            return UserModel(
                uid = user?.uid!!,
                email = user.email!!,
                name = user.displayName!!
            )
        }
    }
}
