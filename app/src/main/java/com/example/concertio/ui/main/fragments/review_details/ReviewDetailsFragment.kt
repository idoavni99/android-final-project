package com.example.concertio.ui.main.fragments.review_details

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.concertio.R
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.extensions.loadReviewImage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewDetailsFragment : Fragment() {
    private val args by navArgs<ReviewDetailsFragmentArgs>()
    private val viewModel: ReviewsViewModel by activityViewModels()
    private val media by lazy { view?.findViewById<ImageView>(R.id.details_image) }
    private val artistTextView by lazy { view?.findViewById<TextView>(R.id.review_artist) }
    private val locationTextView by lazy { view?.findViewById<TextView>(R.id.review_location) }
    private val reviewText by lazy { view?.findViewById<TextView>(R.id.review_text) }
    private val reviewStars by lazy { view?.findViewById<RatingBar>(R.id.details_review_stars) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getReviewById(args.reviewId)
            .observe(viewLifecycleOwner, ::setupTextFields)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupTextFields(reviewObject: ReviewWithReviewer?) {
        reviewObject?.review?.run {
            artistTextView?.text = artist
            locationTextView?.text = location
            reviewText?.text = review
            reviewStars?.rating = stars.toFloat()
            mediaUri?.run {
                media?.loadReviewImage(
                    requireContext(),
                    Uri.parse(this),
                    R.drawable.front_page_logo
                )
            }
        }
    }
}