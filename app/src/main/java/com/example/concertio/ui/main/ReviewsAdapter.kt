package com.example.concertio.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer
import com.example.concertio.ui.main.fragments.reviews_list.ReviewViewHolder
import com.example.concertio.ui.main.fragments.user_profile.UserReviewViewHolder

enum class ReviewType {
    USER,
    REVIEW
}

class ReviewsAdapter(
    private val onReviewClicked: (ReviewModel) -> Unit,
    private val reviewType: ReviewType
) :
    RecyclerView.Adapter<ViewHolder>() {

    private var reviews: List<ReviewWithReviewer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (reviewType) {
            ReviewType.USER -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_review_list_item, parent, false)
                UserReviewViewHolder(itemView)
            }

            ReviewType.REVIEW -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.review_list_item, parent, false)
                ReviewViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReview = reviews[position]
        when (holder) {
            is ReviewViewHolder -> ReviewViewHolder.bind(holder, currentReview, onReviewClicked)
            is UserReviewViewHolder -> UserReviewViewHolder.bind(
                holder,
                currentReview,
                onReviewClicked
            )
        }
    }

    fun updateReviews(newReviews: List<ReviewWithReviewer>) {
        this.reviews = newReviews
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}