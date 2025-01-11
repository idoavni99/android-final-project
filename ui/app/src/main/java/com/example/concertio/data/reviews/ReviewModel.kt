package com.example.concertio.data.reviews

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.concertio.data.ValidationResult
import com.example.concertio.data.users.UserModel
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@Entity(
    tableName = "reviews",
    foreignKeys = [ForeignKey(
        entity = UserModel::class,
        parentColumns = ["uid"],
        childColumns = ["reviewer_uid"]
    )]
)
data class ReviewModel(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "reviewer_uid") val reviewerUid: String,
    @ColumnInfo(name = "artist") val artist: String? = null,
    @ColumnInfo(name = "review") val review: String,
) {
    fun validate(): ValidationResult {
        try {
            require(review.isNotEmpty()) { "Review cannot be empty" }
            return ValidationResult()
        } catch (e: IllegalArgumentException) {
            return ValidationResult(e)
        }
    }

    fun toRemoteSource(): RemoteSourceReview {
        return RemoteSourceReview(
            artist = artist,
            location_name = location,
            review = review,
            reviewer_uid = reviewerUid,
            id = id
        )
    }
}


