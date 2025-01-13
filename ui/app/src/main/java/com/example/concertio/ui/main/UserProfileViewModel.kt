package com.example.concertio.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.reviews.ReviewsRepository
import com.example.concertio.data.users.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val usersRepository = UsersRepository.getInstance()
    private val reviewsRepository = ReviewsRepository.getInstance()

    fun observeMyProfile() =
        usersRepository.getMyUserObservable()

    fun observeMyReviews() = reviewsRepository.getReviewsList(50, 0, true)

    fun signOut(onCompleteUi: () -> Unit) = viewModelScope.launch {
        reviewsRepository.deleteAll()
        usersRepository.deleteAllUsers()
        FirebaseAuth.getInstance().signOut()
        onCompleteUi()
    }

}