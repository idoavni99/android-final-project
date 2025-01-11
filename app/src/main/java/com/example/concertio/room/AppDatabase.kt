package com.example.concertio.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.concertio.data.students.StudentModel

@Database(entities = [StudentModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun studentsDao(): StudentsDAO
}