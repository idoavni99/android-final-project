package com.example.concertio.ui.main.fragments.savestudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.ui.main.UiState
import com.example.concertio.data.reviews.ReviewModel
import com.google.firebase.auth.FirebaseAuth

class SaveReviewFragment : Fragment() {
    private val viewModel: ReviewsViewModel by activityViewModels()
    private val artistTextView by lazy { view?.findViewById<EditText>(R.id.review_artist) }
    private val locationTextView by lazy { view?.findViewById<EditText>(R.id.review_location) }
    private val reviewText by lazy { view?.findViewById<EditText>(R.id.save_review_text) }

    private val cancelButton by lazy { view?.findViewById<Button>(R.id.details_cancel_button) }
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
        viewModel.getUiStateObserver().observe(viewLifecycleOwner) { uiState ->
            setupToolbar(uiState)
            setupActions(view, uiState)
            viewModel.getReviewByUid(uiState.reviewUid)
                .observe(viewLifecycleOwner, ::setupInputFields)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupInputFields(reviewObject: ReviewWithReviewer?) {
        reviewObject?.review?.run {
            artistTextView?.setText(artist)
            locationTextView?.setText(location)
            reviewText?.setText(review)
            this@SaveReviewFragment.reviewerUid = reviewerUid
        }
    }

    private fun setupToolbar(uiState: UiState) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            when (uiState.detailsMode) {
                SaveReviewMode.ADD -> {
                    title = "Add Review"
                    menu[0].isVisible = false
                }

                SaveReviewMode.EDIT -> {
                    title = "Edit Review"
                    menu[0].isVisible = false
                }

                else -> {}
            }
        }
    }

    private fun setupActions(view: View, uiState: UiState) {
        val nav = view.findNavController()
        if (uiState.detailsMode == SaveReviewMode.ADD) {
            deleteButton?.isVisible = false
        }

        deleteButton?.setOnClickListener {
            viewModel.deleteReviewByUid(uiState.reviewUid) {
                nav.navigate(SaveReviewFragmentDirections.actionSaveReviewFragmentToReviewsListFragment())
            }
        }
        saveButton?.setOnClickListener {
            val reviewData = getReviewFromInputs()
            viewModel.saveReview(reviewData, {
                nav.navigate(SaveReviewFragmentDirections.actionSaveReviewFragmentToReviewsListFragment())
            }, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })

        }
        cancelButton?.setOnClickListener {
            nav.navigate(SaveReviewFragmentDirections.actionSaveReviewFragmentToReviewsListFragment())
        }
    }

    private fun getReviewFromInputs() = ReviewModel(
        location = locationTextView?.text.toString(),
        artist = artistTextView?.text.toString(),
        review = reviewText?.text.toString(),
        reviewerUid = reviewerUid
    )
}