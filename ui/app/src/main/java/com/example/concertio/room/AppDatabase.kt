package com.example.concertio.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewsDao
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersDao

@Database(
    entities = [ReviewModel::class, UserModel::class], version = 2, exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewsDao(): ReviewsDao
    abstract fun usersDao(): UsersDao

}