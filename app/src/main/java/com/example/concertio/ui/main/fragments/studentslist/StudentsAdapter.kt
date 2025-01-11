package com.example.concertio.ui.main.fragments.studentslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.concertio.R
import com.example.concertio.data.students.StudentModel

class StudentsAdapter(
    val onStudentChecked: (StudentModel) -> Unit,
    val onStudentClicked: (StudentModel) -> Unit
) :
    RecyclerView.Adapter<StudentsAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.student_name)
        val idTextView: TextView = itemView.findViewById(R.id.student_id)
        val checkbox: CheckBox = itemView.findViewById(R.id.student_checkbox)
    }

    private var students: List<StudentModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = students[position]
        holder.nameTextView.text = currentStudent.name
        holder.idTextView.text = currentStudent.id.toString()
        holder.checkbox.isChecked = currentStudent.checked

        holder.itemView.setOnClickListener {
            onStudentClicked(students[position])
        }

        holder.checkbox.setOnClickListener {
            this.onStudentChecked(
                currentStudent
            )
            notifyItemChanged(position)
        }
    }

    fun updateStudents(newStudents: List<StudentModel>) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    override fun getItemCount(): Int {
        return students.size
    }
}