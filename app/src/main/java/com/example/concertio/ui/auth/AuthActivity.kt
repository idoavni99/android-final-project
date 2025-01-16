package com.example.concertio.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.concertio.R
import com.example.concertio.ui.auth.fragments.LoginFragment
import com.example.concertio.ui.main.MainActivity
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels<AuthViewModel> { ViewModelProvider.NewInstanceFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Firebase.auth.currentUser != null) toApp() else {
            supportFragmentManager.commit {
                replace(R.id.auth_fragment_container, LoginFragment.newInstance())
            }
        }
    }

    private fun toApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}