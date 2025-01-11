package com.example.concertio.ui.main.fragments.savestudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.concertio.R
import com.example.concertio.ui.main.StudentsViewModel
import com.example.concertio.ui.main.UiState
import com.example.concertio.data.students.StudentModel

class SaveStudentFragment : Fragment() {
    private val viewModel: StudentsViewModel by activityViewModels()
    private val idTextView by lazy { view?.findViewById<EditText>(R.id.save_student_id) }
    private val nameTextView by lazy { view?.findViewById<EditText>(R.id.save_student_name) }
    private val phoneTextView by lazy { view?.findViewById<EditText>(R.id.save_student_phone) }
    private val addressTextView by lazy { view?.findViewById<EditText>(R.id.student_details_address) }
    private val checkBox by lazy { view?.findViewById<CheckBox>(R.id.student_details_checkbox) }

    private val cancelButton by lazy { view?.findViewById<Button>(R.id.details_cancel_button) }
    private val deleteButton by lazy { view?.findViewById<Button>(R.id.details_delete_button) }
    private val saveButton by lazy { view?.findViewById<Button>(R.id.details_save_button) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_student, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getUiStateObserver().observe(viewLifecycleOwner) { uiState ->
            setupToolbar(uiState)
            setupActions(view, uiState)
            viewModel.getStudentByUid(uiState.studentUid)
                .observe(viewLifecycleOwner, ::setupInputFields)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupInputFields(student: StudentModel?) {
        student?.run {
            idTextView?.setText(id.toString())
            nameTextView?.setText(name)
            phoneTextView?.setText(phone)
            addressTextView?.setText(email)
            checkBox?.isChecked = checked
        }
    }

    private fun setupToolbar(uiState: UiState) {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.apply {
            when (uiState.detailsMode) {
                StudentDetailsMode.ADD -> {
                    title = "Add Student"
                    menu[0].isVisible = false
                }

                StudentDetailsMode.EDIT -> {
                    title = "Edit Student"
                    menu[0].isVisible = false
                }

                else -> {}
            }
        }
    }

    private fun setupActions(view: View, uiState: UiState) {
        val nav = view.findNavController()
        if (uiState.detailsMode == StudentDetailsMode.ADD) {
            deleteButton?.isVisible = false
        }

        deleteButton?.setOnClickListener {
            viewModel.deleteStudentByUid(uiState.studentUid) {
                nav.navigate(SaveStudentFragmentDirections.actionSaveStudentFragmentToStudentsListFragment())
            }
        }
        saveButton?.setOnClickListener {
            val studentData = getStudentFromInputs()
            viewModel.saveStudent(studentData, {
                nav.navigate(SaveStudentFragmentDirections.actionSaveStudentFragmentToStudentsListFragment())
            }, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })

        }
        cancelButton?.setOnClickListener {
            nav.navigate(SaveStudentFragmentDirections.actionSaveStudentFragmentToStudentsListFragment())
        }
    }

    private fun getStudentFromInputs() = StudentModel(
        name = nameTextView?.text.toString(),
        id = idTextView?.text.toString().toIntOrNull() ?: 0,
        phone = phoneTextView?.text.toString(),
        email = addressTextView?.text.toString(),
        checked = checkBox?.isChecked ?: false
    )
}