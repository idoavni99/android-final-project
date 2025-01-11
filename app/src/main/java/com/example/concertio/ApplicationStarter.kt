package com.example.concertio

import android.app.Application
import com.example.concertio.room.DatabaseHolder
import com.firebase.ui.auth.AuthUI

class ApplicationStarter: Application() {
    override fun onCreate() {
        DatabaseHolder.initDatabase(this)
        super.onCreate()
    }
}