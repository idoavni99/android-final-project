package com.example.concertio.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.concertio.R
import com.example.concertio.ui.main.MainActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

const val FIREBASE_SIGN_IN_CONST = 1

class AuthActivity : AppCompatActivity() {
    private val signInLauncher by lazy {
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            this.onSignInResult(it)
        }
    }

    private val supportedAuth = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(supportedAuth)
            .setLogo(R.drawable.ic_launcher_foreground)
            .setTheme(R.style.Base_Theme_ConcertIO)
            .build().apply {
                signInLauncher.launch(this)
            }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}