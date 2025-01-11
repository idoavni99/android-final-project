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

data class UiState(val reviewId: String = "", val detailsMode: SaveReviewMode? = null)

class ReviewsViewModel : ViewModel() {
    private val repository = ReviewsRepository()
    private val uiState = MutableLiveData(UiState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadReviewsFromRemoteSource(50, 0)
        }
    }

    fun getUiStateObserver(): LiveData<UiState> {
        return this.uiState
    }

    fun deleteReviewById(id: String, onDeletedUi: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteReviewById(id)
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
            val uid = uiState.value?.reviewId ?: return@launch
            if (uid.isNotEmpty()) {
                repository.loadReviewFromRemoteSource(uid)
            }
        }
    }

    fun getReviewById(id: String = ""): LiveData<ReviewWithReviewer?> {
        return this.repository.getReviewById(id)
    }

    fun saveReview(
        review: ReviewModel,
        onCompleteUi: () -> Unit = {},
        onErrorUi: (message: String?) -> Unit = {}
    ) {
        review.validate().let {
            viewModelScope.launch(Dispatchers.Main) {
                if (it.success) {
                    when (uiState.value?.detailsMode) {
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
        this.updateUiState(UiState(reviewId = id))
    }

    fun toSaveReview(id: String, mode: SaveReviewMode) {
        this.updateUiState(UiState(reviewId = id, detailsMode = mode))
    }

    fun toReviewsList() {
        this.updateUiState(UiState())
    }

    private fun updateUiState(newState: UiState) {
        uiState.value = newState
    }
}