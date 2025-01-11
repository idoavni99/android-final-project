package com.example.concertio.ui.main.fragments.studentslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.concertio.R
import com.example.concertio.ui.main.ReviewsViewModel
import com.example.concertio.ui.main.fragments.savestudent.SaveReviewMode


class ReviewsListFragment : Fragment() {
    private lateinit var reviewsList: RecyclerView
    private val toolbar by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }
    private val viewModel: ReviewsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewsList = view.findViewById(R.id.students_list)
        context?.let { initStudentsList(it) }
        viewModel.getAllReviews().observe(viewLifecycleOwner, {
            (reviewsList.adapter as? ReviewsAdapter)?.updateReviews(it)
        })
        viewModel.getUiStateObserver().observe(viewLifecycleOwner, {
            setupToolbar(view)
        })
    }

    private fun setupToolbar(view: View) {
        toolbar?.apply {
            navigationIcon = null
            menu[0].apply {
                title = "Add"
                isVisible = true
            }

            setOnMenuItemClickListener {
                viewModel.toSaveReview("", SaveReviewMode.ADD)
                view.findNavController()
                    .navigate(ReviewsListFragmentDirections.actionReviewsListFragmentToSaveReviewFragment())
                true
            }
        }
    }

    private fun initStudentsList(context: Context) {
        reviewsList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = ReviewsAdapter { (uid) ->
                viewModel.toReviewDetails(uid)
                findNavController().navigate(ReviewsListFragmentDirections.actionReviewsListFragmentToReviewDetailsFragment())
            }
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}