package com.example.concertio.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.ui.main.fragments.savestudent.StudentDetailsMode
import com.example.concertio.data.students.StudentModel
import com.example.concertio.data.students.StudentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class UiState(val studentUid: String = "", val detailsMode: StudentDetailsMode? = null)

class StudentsViewModel : ViewModel() {
    private val repository = StudentsRepository()
    private val uiState = MutableLiveData(UiState())

    fun getUiStateObserver(): LiveData<UiState> {
        return this.uiState
    }

    fun deleteStudentByUid(uid: String, onDeletedUi: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteStudentByUid(uid)
            onDeletedUi()
        }
    }

    fun getAllStudents(): LiveData<List<StudentModel>> {
        return this.repository.getStudentsList()
    }

    fun getStudentByUid(uid: String = ""): LiveData<StudentModel?> {
        return this.repository.getStudentByUid(uid)
    }

    fun checkStudent(student: StudentModel) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.editStudent(student.copy(checked = !student.checked))
        }
    }

    fun saveStudent(
        student: StudentModel,
        onCompleteUi: () -> Unit = {},
        onErrorUi: (message: String?) -> Unit = {}
    ) {
        student.validate().let {
            viewModelScope.launch(Dispatchers.Main) {
                if (it.success) {
                    when (uiState.value?.detailsMode) {
                        StudentDetailsMode.ADD -> repository.addStudent(student)
                        StudentDetailsMode.EDIT -> repository.editStudent(student)
                        else -> {}
                    }
                    onCompleteUi()
                } else {
                    onErrorUi(it.message)
                }
            }
        }
    }

    fun toStudentDetails(uid: String) {
        this.updateUiState(UiState(studentUid = uid))
    }

    fun toSaveStudent(uid: String, mode: StudentDetailsMode) {
        this.updateUiState(UiState(studentUid = uid, detailsMode = mode))
    }

    fun toStudentsList() {
        this.updateUiState(UiState())
    }

    private fun updateUiState(newState: UiState) {
        uiState.value = newState
    }
}