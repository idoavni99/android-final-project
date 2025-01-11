package com.example.concertio.ui.main.fragments.studentdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.ui.main.StudentsViewModel
import com.example.concertio.ui.main.UiState
import com.example.concertio.ui.main.fragments.savestudent.StudentDetailsMode
import com.example.concertio.data.students.StudentModel

class StudentDetailsFragment : Fragment() {
    private val viewModel: StudentsViewModel by activityViewModels()
    private val toolbar by lazy { activity?.findViewById<Toolbar>(R.id.toolbar) }
    private val idTextView by lazy { view?.findViewById<TextView>(R.id.save_student_id) }
    private val nameTextView by lazy { view?.findViewById<TextView>(R.id.save_student_name) }
    private val phoneTextView by lazy { view?.findViewById<TextView>(R.id.save_student_phone) }
    private val addressTextView by lazy { view?.findViewById<TextView>(R.id.student_details_address) }
    private val checkBox by lazy { view?.findViewById<CheckBox>(R.id.student_details_checkbox) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getUiStateObserver().observe(viewLifecycleOwner) { uiState ->
            setupToolbar(view, uiState)
            viewModel.getStudentByUid(uiState.studentUid)
                .observe(viewLifecycleOwner, ::setupTextFields)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupTextFields(studentData: StudentModel?) {
        studentData?.run {
            idTextView?.text = id.toString()
            nameTextView?.text = name
            phoneTextView?.text = phone
            addressTextView?.text = email
            checkBox?.isChecked = checked
        }
    }

    private fun setupToolbar(view: View, uiState: UiState) {
        toolbar?.apply {
            menu[0].apply {
                title = "Edit"
                isVisible = true
            }
            setOnMenuItemClickListener {
                viewModel.toSaveStudent(uiState.studentUid, StudentDetailsMode.EDIT)
                view.findNavController()
                    .navigate(StudentDetailsFragmentDirections.actionStudentDetailsFragmentToSaveStudentFragment())
                true
            }
        }
    }
}