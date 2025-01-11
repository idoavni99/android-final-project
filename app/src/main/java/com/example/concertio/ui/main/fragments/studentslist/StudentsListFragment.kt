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
import com.example.concertio.ui.main.StudentsViewModel
import com.example.concertio.ui.main.fragments.savestudent.StudentDetailsMode
import com.example.concertio.ui.main.fragments.studentslist.StudentsListFragmentDirections.Companion.actionStudentsListFragmentToStudentDetailsFragment


class StudentsListFragment : Fragment() {
    private lateinit var studentsList: RecyclerView
    private val toolbar by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }
    private val viewModel: StudentsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentsList = view.findViewById(R.id.students_list)
        context?.let { initStudentsList(it) }
        viewModel.getAllStudents().observe(viewLifecycleOwner, {
            (studentsList.adapter as? StudentsAdapter)?.updateStudents(it)
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
                viewModel.toSaveStudent("", StudentDetailsMode.ADD)
                view.findNavController()
                    .navigate(StudentsListFragmentDirections.actionStudentsListFragmentToSaveStudentFragment())
                true
            }
        }
    }

    private fun initStudentsList(context: Context) {
        studentsList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = StudentsAdapter({ viewModel.checkStudent(it) }, { (uid) ->
                viewModel.toStudentDetails(uid)
                findNavController().navigate(actionStudentsListFragmentToStudentDetailsFragment())
            })
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}