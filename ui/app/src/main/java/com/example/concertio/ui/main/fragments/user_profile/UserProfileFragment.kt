package com.example.concertio.ui.main.fragments.user_profile

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.concertio.R
import com.example.concertio.extensions.loadBase64
import com.example.concertio.ui.auth.AuthActivity
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.ui.main.UserProfileViewModel
import com.example.concertio.ui.main.fragments.reviews_list.ReviewsAdapter
import com.example.concertio.ui.main.fragments.save_review.SaveReviewMode

class UserProfileFragment : Fragment() {
    private val reviewsViewModel: ReviewsViewModel by activityViewModels()
    private val viewModel: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.sign_out_button)?.setOnClickListener {
            activity?.run {
                viewModel.signOut {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
            }
        }
        setupFields(view)
        setupMyReviewsList(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupFields(view: View) {
        viewModel.observeMyProfile().observe(viewLifecycleOwner) { user ->
            user?.run {
                view.findViewById<TextView>(R.id.user_name_text)?.text = name
                profilePicture?.let { picUrlOrB64 ->
                    view.findViewById<ImageView>(R.id.userProfilePic)?.loadBase64(
                        this@UserProfileFragment,
                        picUrlOrB64,
                        R.drawable.empty_profile_picture
                    )
                }
            }
        }
    }

    private fun setupMyReviewsList(view: View) {
        viewModel.observeMyReviews().observe(viewLifecycleOwner) {
            view.findViewById<RecyclerView>(R.id.myReviewsList)?.run {
                adapter = ReviewsAdapter {
                    reviewsViewModel.toSaveReview(it.id, SaveReviewMode.EDIT)
                    findNavController().navigate(
                        UserProfileFragmentDirections.actionUserProfileFragmentToEditReviewFragment()
                    )
                }.apply {
                    updateReviews(it)
                }
                layoutManager = LinearLayoutManager(context)

                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        LinearLayoutManager.VERTICAL
                    )
                )
            }
        }
    }
}