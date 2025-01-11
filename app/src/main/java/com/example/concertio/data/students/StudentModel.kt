package com.example.concertio.data.students

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.concertio.data.ValidationResult
import java.util.UUID

@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey() val uid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "checked") val checked: Boolean = false,
) {
    fun validate(): ValidationResult {
        try {
            require(name.isNotEmpty()) { "Name cannot be empty" }
            require(id > 0) { "ID must be a positive integer" }
            return ValidationResult()
        } catch (e: IllegalArgumentException) {
            return ValidationResult(e)
        }
    }
}
