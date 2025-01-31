package com.example.concertio.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.reviews.ReviewsRepository
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class UserProfileViewModel : ViewModel() {
    private val usersRepository = UsersRepository.getInstance()
    private val reviewsRepository = ReviewsRepository.getInstance()

    fun observeMyProfile() =
        usersRepository.getMyUserObservable()

    fun observeMyReviews() = reviewsRepository.getReviewsList(50, 0, true)

    fun deleteReviewById(id: String) = viewModelScope.launch {
        reviewsRepository.deleteReviewById(id)
    }

    fun signOut() = viewModelScope.launch {
        reviewsRepository.deleteAll()
        usersRepository.deleteAllUsers()
        FirebaseAuth.getInstance().signOut()
    }

    fun updateProfile(
        name: String,
        photoUri: Uri,
        onCompleteUi: () -> Unit
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            FirebaseAuth.getInstance().currentUser?.let {
                if (photoUri.scheme != "https") {
                    val profilePictureUri = usersRepository.uploadUserProfilePictureToFirebase(
                        photoUri,
                        it.uid
                    )
                    usersRepository.updateUserDetails(name, profilePictureUri!!)
                } else {
                    usersRepository.updateUserDetails(name, photoUri)
                }
                onCompleteUi()
            }
        }

    fun updateAuth(
        email: String?,
        password: String?,
        onCompleteUi: () -> Unit
    ) = viewModelScope.launch {
        if (email == null && password == null) return@launch
        usersRepository.updateUserAuth(email, password)
        onCompleteUi()
    }
}