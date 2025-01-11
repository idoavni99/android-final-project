package com.example.concertio.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.concertio.data.students.StudentModel

@Dao
interface StudentsDAO {
    @Query("SELECT * FROM students")
    fun getAll(): LiveData<List<StudentModel>>

    @Query("SELECT * FROM students WHERE uid IN (:studentIds)")
    fun loadAllByIds(studentIds: IntArray): LiveData<List<StudentModel>>

    @Query("SELECT * FROM students WHERE uid LIKE :uid LIMIT 1")
    fun findByUid(uid: String): LiveData<StudentModel?>

    @Insert
    fun insertAll(vararg students: StudentModel)

    @Delete
    fun delete(student: StudentModel)

    @Query("DELETE FROM students WHERE uid = :uid")
    fun deleteByUid(uid: String)

    @Query("DELETE FROM students")
    fun deleteAll()

    @Update
    fun update(student: StudentModel)
}