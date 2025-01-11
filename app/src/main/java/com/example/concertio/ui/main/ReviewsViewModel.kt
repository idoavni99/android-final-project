package com.example.concertio.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.ui.main.fragments.savestudent.SaveReviewMode
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.data.reviews.ReviewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class UiState(val reviewUid: String = "", val detailsMode: SaveReviewMode? = null)

class ReviewsViewModel : ViewModel() {
    private val repository = ReviewsRepository()
    private val uiState = MutableLiveData(UiState())

    fun getUiStateObserver(): LiveData<UiState> {
        return this.uiState
    }

    fun deleteReviewByUid(uid: String, onDeletedUi: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteStudentByUid(uid)
            onDeletedUi()
        }
    }

    fun getAllReviews(): LiveData<List<ReviewWithReviewer>> {
        return this.repository.getStudentsList()
    }

    fun getReviewByUid(uid: String = ""): LiveData<ReviewWithReviewer?> {
        return this.repository.getStudentByUid(uid)
    }

    fun saveReview(
        student: ReviewModel,
        onCompleteUi: () -> Unit = {},
        onErrorUi: (message: String?) -> Unit = {}
    ) {
        student.validate().let {
            viewModelScope.launch(Dispatchers.Main) {
                if (it.success) {
                    when (uiState.value?.detailsMode) {
                        SaveReviewMode.ADD -> repository.addStudent(student)
                        SaveReviewMode.EDIT -> repository.editStudent(student)
                        else -> {}
                    }
                    onCompleteUi()
                } else {
                    onErrorUi(it.message)
                }
            }
        }
    }

    fun toReviewDetails(uid: String) {
        this.updateUiState(UiState(reviewUid = uid))
    }

    fun toSaveReview(uid: String, mode: SaveReviewMode) {
        this.updateUiState(UiState(reviewUid = uid, detailsMode = mode))
    }

    fun toReviewsList() {
        this.updateUiState(UiState())
    }

    private fun updateUiState(newState: UiState) {
        uiState.value = newState
    }
}