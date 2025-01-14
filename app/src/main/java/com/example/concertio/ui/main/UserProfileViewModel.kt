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

    fun signOut() = viewModelScope.launch {
        reviewsRepository.deleteAll()
        usersRepository.deleteAllUsers()
        FirebaseAuth.getInstance().signOut()
    }

    fun updateProfile(name: String, email: String, password: String, photoUri: Uri, onCompleteUi: ()->Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            val userInFirebase = FirebaseAuth.getInstance().currentUser
            var destinationModel = UserModel(name = name, email = email)
            userInFirebase?.let {
                if (photoUri.scheme !== "content") {
                    val profilePictureUri = usersRepository.uploadUserProfilePictureToFirebase(
                        photoUri,
                        userInFirebase.uid
                    )
                    destinationModel =
                        destinationModel.copy(profilePicture = profilePictureUri.toString())
                }
                usersRepository.updateUserDetails(
                    destinationModel.copy(uid = userInFirebase.uid),
                    password
                )
                onCompleteUi()
            }
        }
}