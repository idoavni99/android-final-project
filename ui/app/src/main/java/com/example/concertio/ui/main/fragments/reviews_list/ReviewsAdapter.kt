package com.example.concertio.ui.main.fragments.reviews_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.concertio.R
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewWithReviewer

class ReviewsAdapter(
    val onReviewClicked: (ReviewModel) -> Unit
) :
    RecyclerView.Adapter<ReviewsAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reviewerUid: TextView = itemView.findViewById(R.id.reviewer_uid)
        val locationAndArtist: TextView = itemView.findViewById(R.id.review_location_artist)
    }

    private var reviews: List<ReviewWithReviewer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentReview = reviews[position]
        holder.reviewerUid.text = currentReview.reviewer.name
        holder.locationAndArtist.text =
            "${currentReview.review.location} * ${currentReview.review.artist}"

        holder.itemView.setOnClickListener {
            onReviewClicked(reviews[position].review)
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