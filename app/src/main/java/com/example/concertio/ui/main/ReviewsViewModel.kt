package com.example.concertio.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.data.reviews.ReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewsViewModel : ViewModel() {
    private val repository = ReviewsRepository.getInstance()

    fun deleteReviewById(id: String, onDeletedUi: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteReviewById(id)
            onDeletedUi()
        }
    }

    fun getReviews(shouldGetMyReviews: Boolean = false): LiveData<List<ReviewWithReviewer>> {
        return this.repository.getReviewsList(50, 0, shouldGetMyReviews)
    }

    fun invalidateReviews() {
        viewModelScope.launch {
            repository.loadReviewsFromRemoteSource(50, 0)
        }
    }

    fun invalidateReviewById(id: String) {
        viewModelScope.launch {
            repository.loadReviewFromRemoteSource(id)
        }
    }

    fun getReviewById(id: String): LiveData<ReviewWithReviewer?> {
        return this.repository.getReviewById(id)
    }

    fun saveReview(
        review: ReviewModel,
        filePath: Uri? = null,
        onCompleteUi: () -> Unit = {},
        onErrorUi: (message: String?) -> Unit = {}
    ) {
        review.validate().let {
            viewModelScope.launch(Dispatchers.Main) {
                if (it.success) {
                    val mediaUri = filePath?.let { uri ->
                        repository.uploadReviewMedia(review.id, uri)
                    }
                    repository.saveReview(review.copy(mediaUri = mediaUri?.toString()))
                    onCompleteUi()
                } else {
                    onErrorUi(it.message)
                }
            }
        }
    }
}