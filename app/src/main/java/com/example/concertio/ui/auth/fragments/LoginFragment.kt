package com.example.concertio.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.concertio.R
import com.example.concertio.ui.auth.AuthViewModel
import com.example.concertio.ui.main.MainActivity
import com.google.android.gms.common.SignInButton
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginFragment : Fragment() {
    private val authViewModel by activityViewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupGoogleSignIn(view)
        view.findViewById<MaterialButton>(R.id.email_sign_in_button).setOnClickListener {
            toEmailSignUp()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupGoogleSignIn(view: View) {
        val credentialManager = CredentialManager.create(requireContext())
        view.findViewById<SignInButton>(R.id.google_sign_in_button).setOnClickListener {
            val idOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .setNonce(getString(R.string.app_name))
                .build()
            authViewModel.signInWithIdToken(idOption, credentialManager, requireContext()) {
                toApp()
            }
        }
    }

    private fun toEmailSignUp() {
        activity?.run {
            supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(
                R.id.auth_fragment_container,
                EmailFragment.newInstance()
            ).commit()
        }
    }

    private fun toApp() {
        activity?.run {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}