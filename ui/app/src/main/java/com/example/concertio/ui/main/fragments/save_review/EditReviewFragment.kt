package com.example.concertio.ui.main.fragments.save_review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.ui.main.ReviewsUiState
import com.example.concertio.data.reviews.ReviewModel
import com.google.firebase.auth.FirebaseAuth

class EditReviewFragment : Fragment() {
    private val viewModel: ReviewsViewModel by activityViewModels()
    private val artistTextView by lazy { view?.findViewById<EditText>(R.id.review_artist) }
    private val locationTextView by lazy { view?.findViewById<EditText>(R.id.review_location) }
    private val reviewText by lazy { view?.findViewById<EditText>(R.id.save_review_text) }

    private val deleteButton by lazy { view?.findViewById<Button>(R.id.details_delete_button) }
    private val saveButton by lazy { view?.findViewById<Button>(R.id.details_save_button) }

    private var reviewerUid: String = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.observeUiState().observe(viewLifecycleOwner) { uiState ->
            setupActions(view, uiState)
        }

        viewModel.getReviewById().observe(viewLifecycleOwner, ::setupInputFields)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupInputFields(reviewObject: ReviewWithReviewer?) {
        reviewObject?.review?.run {
            artistTextView?.setText(artist)
            locationTextView?.setText(location)
            reviewText?.setText(review)
            this@EditReviewFragment.reviewerUid = reviewerUid
        } ?: {
            viewModel.invalidateReviewById()
        }
    }

    private fun setupActions(view: View, reviewsUiState: ReviewsUiState) {
        if (reviewsUiState.detailsMode == SaveReviewMode.ADD) {
            deleteButton?.isVisible = false
        }

        deleteButton?.setOnClickListener {
            viewModel.deleteReviewByCurrentId() {
                toReviewsList(view)
            }
        }
        saveButton?.setOnClickListener {
            val reviewData = getReviewFromInputs(reviewsUiState)
            viewModel.editReview(reviewData,
                onCompleteUi = {
                    toReviewsList(view)
                },
                onErrorUi = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )

        }
    }

    private fun toReviewsList(view: View) {
        val nav = view.findNavController()
        viewModel.toReviewsList()
        nav.navigate(EditReviewFragmentDirections.actionEditReviewFragmentToUserProfileFragment())
    }

    private fun getReviewFromInputs(uiState: ReviewsUiState) = ReviewModel(
        location = locationTextView?.text.toString(),
        artist = artistTextView?.text.toString(),
        review = reviewText?.text.toString(),
        reviewerUid = reviewerUid,
        id = uiState.reviewId
    )
}