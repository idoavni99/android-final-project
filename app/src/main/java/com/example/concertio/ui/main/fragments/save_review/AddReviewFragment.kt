package com.example.concertio.ui.main.fragments.save_review

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.extensions.FileUploadingFragment
import com.example.concertio.extensions.showProgress
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

open class AddReviewFragment : FileUploadingFragment() {
    var mediaType: String? = null
    var mediaUri: Uri? = null
    protected val viewModel: ReviewsViewModel by activityViewModels()
    protected val reviewMedia by lazy { view?.findViewById<ImageView>(R.id.save_review_image) }
    protected val artistTextView by lazy { view?.findViewById<EditText>(R.id.review_artist) }
    protected val locationTextView by lazy { view?.findViewById<EditText>(R.id.review_location) }
    protected val reviewText by lazy { view?.findViewById<EditText>(R.id.save_review_text) }
    protected val reviewStars by lazy { view?.findViewById<RatingBar>(R.id.save_review_stars) }
    protected val deleteButton by lazy { view?.findViewById<MaterialButton>(R.id.details_delete_button) }
    protected val saveButton by lazy { view?.findViewById<MaterialButton>(R.id.details_save_button) }
    protected var reviewerUid: String = FirebaseAuth.getInstance().currentUser!!.uid

    private val selectMediaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    reviewMedia?.setImageURI(it)
                    mediaUri = it
                    mediaType =
                        context?.contentResolver?.getType(it)?.split("/")?.get(0)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    protected open fun setupView(view: View) {
        setupActions(view)
    }

    private fun setupActions(view: View) {
        deleteButton?.isVisible = false

        saveButton?.setOnClickListener {
            saveButton?.showProgress(resources.getColor(com.google.android.material.R.color.design_default_color_secondary))
            val reviewData = getReviewFromInputs()
            viewModel.saveReview(reviewData, mediaUri,
                onCompleteUi = {
                    this.onReviewSaved(view)
                },
                onErrorUi = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }

        reviewMedia?.setOnClickListener {
            requestFileAccess()
        }
    }

    override fun onFileAccessGranted() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        selectMediaLauncher.launch(intent)
    }

    open fun onReviewSaved(view: View) {
        view.findNavController()
            .navigate(AddReviewFragmentDirections.actionAddReviewFragmentToReviewsListFragment())
    }

    private fun getReviewFromInputs() = ReviewModel(
        location = locationTextView?.text.toString(),
        artist = artistTextView?.text.toString(),
        review = reviewText?.text.toString(),
        reviewerUid = reviewerUid,
        id = UUID.randomUUID().toString(),
        stars = reviewStars?.rating?.toLong() ?: 4,
        mediaType = mediaType,
    )
}