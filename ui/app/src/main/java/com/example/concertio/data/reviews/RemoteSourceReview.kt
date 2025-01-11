package com.example.concertio.data.reviews

import com.google.firebase.firestore.GeoPoint

data class RemoteSourceReview(
    val artist: String? = null,
    val location_coordinate: GeoPoint? = null,
    val location_name: String? = null,
    val review: String? = null,
    val reviewer_uid: String? = null,
    val media_id: String? = null,
    val media_type: String? = null,
    val id: String? = null
) {
    fun toReviewModel(): ReviewModel {
        return ReviewModel(
            id=id ?: "",
            artist = artist,
            location = location_name,
            review = review ?: "",
            reviewerUid = reviewer_uid ?: "",
        )
    }
}