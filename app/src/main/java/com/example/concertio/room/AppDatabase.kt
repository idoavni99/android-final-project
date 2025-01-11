package com.example.concertio.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.concertio.data.reviews.ReviewModel
import com.example.concertio.data.reviews.ReviewsDao
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersDao

@Database(
    entities = [ReviewModel::class, UserModel::class], version = 4, autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ], exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewsDao(): ReviewsDao
    abstract fun usersDao(): UsersDao
}