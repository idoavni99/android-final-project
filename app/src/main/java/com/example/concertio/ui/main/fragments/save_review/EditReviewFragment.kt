package com.example.concertio.ui.main.fragments.save_review

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.extensions.loadReviewImage
import com.example.concertio.extensions.showProgress

class EditReviewFragment : AddReviewFragment() {
    private val args by navArgs<EditReviewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_review, container, false)
    }

    override fun setupView(view: View) {
        super.setupView(view)
        setupDeleteButton(view)
        viewModel.getReviewById(args.reviewId).observe(viewLifecycleOwner, ::setupInputFields)
    }

    private fun setupInputFields(reviewObject: ReviewWithReviewer?) {
        reviewObject?.review?.run {
            artistTextView?.setText(artist)
            locationTextView?.setText(location)
            reviewText?.setText(review)
            reviewStars?.rating = stars.toFloat()
            this@EditReviewFragment.reviewerUid = reviewerUid
            mediaUri?.let {
                reviewMedia?.loadReviewImage(
                    requireContext(),
                    Uri.parse(mediaUri),
                    R.drawable.baseline_insert_photo_24
                )
            }
        } ?: {
            viewModel.invalidateReviewById(args.reviewId)
        }
    }

    private fun setupDeleteButton(view: View) {
        deleteButton?.isVisible = true
        deleteButton?.setOnClickListener {
            deleteButton?.showProgress(resources.getColor(com.google.android.material.R.color.design_default_color_secondary))
            viewModel.deleteReviewById(args.reviewId) {
                view.findNavController()
                    .navigate(EditReviewFragmentDirections.actionEditReviewFragmentToUserProfileFragment())
            }
        }
    }

    override fun onReviewSaved(view: View) {
        view.findNavController()
            .navigate(EditReviewFragmentDirections.actionEditReviewFragmentToUserProfileFragment())
    }
}