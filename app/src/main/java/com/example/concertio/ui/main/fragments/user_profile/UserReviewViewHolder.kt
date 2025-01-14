package com.example.concertio.ui.main.fragments.user_profile

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer

class UserReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val reviewLocation: TextView = itemView.findViewById(R.id.review_location)
    val reviewArtist: TextView = itemView.findViewById(R.id.review_artist)
    val reviewText: TextView = itemView.findViewById(R.id.review_text)
    val stars: RatingBar = itemView.findViewById(R.id.user_review_stars)

    companion object {
        fun bind(
            holder: UserReviewViewHolder,
            currentReview: ReviewWithReviewer,
            onReviewClicked: (review: ReviewModel) -> Unit
        ) {
            holder.reviewLocation.text = currentReview.review.location
            holder.reviewArtist.text = currentReview.review.artist
            holder.reviewText.text = currentReview.review.review
            holder.stars.rating = currentReview.review.stars.toFloat()
            holder.itemView.setOnClickListener {
                onReviewClicked(currentReview.review)
            }
        }
    }
}