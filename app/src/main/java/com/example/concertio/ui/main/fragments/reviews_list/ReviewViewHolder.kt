package com.example.concertio.ui.main.fragments.reviews_list

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.extensions.loadProfilePicture

class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val reviewerUid: TextView = itemView.findViewById(R.id.reviewer)
    val locationAndArtist: TextView = itemView.findViewById(R.id.review_location_artist)
    val profileImage: ImageView = itemView.findViewById(R.id.reviewer_image)
    val stars: RatingBar = itemView.findViewById(R.id.review_stars)

    companion object {
        fun bind(
            holder: ReviewViewHolder,
            currentReview: ReviewWithReviewer,
            onReviewClicked: (ReviewModel) -> Unit
        ) {
            holder.reviewerUid.text = currentReview.reviewer.name
            holder.locationAndArtist.text =
                "${currentReview.review.location} * ${currentReview.review.artist}"
            currentReview.reviewer.profilePicture?.let {
                holder.profileImage.loadProfilePicture(
                    holder.itemView.context,
                    Uri.parse(it),
                    R.drawable.empty_profile_picture
                )
            }
            holder.stars.rating = currentReview.review.stars.toFloat()
            holder.itemView.setOnClickListener {
                onReviewClicked(currentReview.review)
            }
        }
    }
}