package com.example.concertio.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.ui.main.fragments.save_review.SaveReviewMode
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.data.reviews.ReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ReviewsUiState(val reviewId: String = "", val detailsMode: SaveReviewMode? = null)

class ReviewsViewModel : ViewModel() {
    private val repository = ReviewsRepository()
    private var reviewsUiState = ReviewsUiState()
    private val uiStateObserver = MutableLiveData(reviewsUiState)
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadReviewsFromRemoteSource(50, 0)
        }
    }

    fun observeUiState(): LiveData<ReviewsUiState> {
        return this.uiStateObserver
    }

    fun deleteReviewByCurrentId(onDeletedUi: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteReviewById(reviewsUiState.reviewId)
            onDeletedUi()
        }
    }

    fun getAllReviews(): LiveData<List<ReviewWithReviewer>> {
        return this.repository.getReviewsList(50, 0, viewModelScope)
    }

    fun invalidateReviews() {
        viewModelScope.launch {
            repository.loadReviewsFromRemoteSource(50, 0)
        }
    }

    fun invalidateReviewById() {
        viewModelScope.launch {
            if (reviewsUiState.reviewId.isNotEmpty()) {
                repository.loadReviewFromRemoteSource(reviewsUiState.reviewId)
            }
        }
    }

    fun getReviewById(): LiveData<ReviewWithReviewer?> {
        return this.repository.getReviewById(reviewsUiState.reviewId)
    }

    fun saveReview(
        review: ReviewModel,
        onCompleteUi: () -> Unit = {},
        onErrorUi: (message: String?) -> Unit = {}
    ) {
        review.validate().let {
            viewModelScope.launch(Dispatchers.Main) {
                if (it.success) {
                    when (reviewsUiState.detailsMode) {
                        SaveReviewMode.ADD -> repository.addReview(review)
                        SaveReviewMode.EDIT -> repository.editReview(review)
                        else -> {}
                    }
                    onCompleteUi()
                } else {
                    onErrorUi(it.message)
                }
            }
        }
    }

    fun toReviewDetails(id: String) {
        this.updateUiState(ReviewsUiState(reviewId = id))
    }

    fun toSaveReview(id: String, mode: SaveReviewMode) {
        this.updateUiState(ReviewsUiState(reviewId = id, detailsMode = mode))
    }

    fun toReviewsList() {
        this.updateUiState(ReviewsUiState())
    }

    private fun updateUiState(newState: ReviewsUiState) {
        reviewsUiState = newState;
        uiStateObserver.value = newState
    }
}