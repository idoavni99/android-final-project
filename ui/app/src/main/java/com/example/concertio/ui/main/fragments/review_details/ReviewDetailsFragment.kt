package com.example.concertio.ui.main.fragments.review_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.ui.main.ReviewsUiState
import com.example.concertio.ui.main.fragments.save_review.SaveReviewMode
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReviewDetailsFragment : Fragment() {
    private val viewModel: ReviewsViewModel by activityViewModels()
    private val artistTextView by lazy { view?.findViewById<TextView>(R.id.review_artist) }
    private val locationTextView by lazy { view?.findViewById<TextView>(R.id.review_location) }
    private val reviewText by lazy { view?.findViewById<TextView>(R.id.review_text) }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getReviewById()
            .observe(viewLifecycleOwner, ::setupTextFields)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupTextFields(reviewObject: ReviewWithReviewer?) {
        reviewObject?.review?.run {
            artistTextView?.text = artist
            locationTextView?.text = location
            reviewText?.text = review
        }
    }
}