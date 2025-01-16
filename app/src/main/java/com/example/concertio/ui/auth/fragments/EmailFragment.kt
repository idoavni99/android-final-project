package com.example.concertio.ui.auth.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.concertio.R
import com.example.concertio.extensions.FileUploadingFragment
import com.example.concertio.extensions.loadProfilePicture
import com.example.concertio.extensions.showProgress
import com.example.concertio.extensions.stopProgress
import com.example.concertio.ui.auth.AuthViewModel
import com.example.concertio.ui.main.MainActivity
import com.google.android.material.button.MaterialButton

class EmailFragment : FileUploadingFragment() {
    private var profilePictureUri: Uri? = null
    private val authViewModel by activityViewModels<AuthViewModel>()
    private val emailField by lazy { view?.findViewById<EditText>(R.id.login_email_text) }
    private val passwordField by lazy { view?.findViewById<EditText>(R.id.signup_password) }
    private val displayNameField by lazy { view?.findViewById<EditText>(R.id.signup_displayName) }
    private val profilePicture by lazy { view?.findViewById<ImageView>(R.id.signup_profile_picture) }
    private val actionButton by lazy { view?.findViewById<MaterialButton>(R.id.signup_action_button) }
    private val selectMediaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    profilePicture?.loadProfilePicture(
                        requireContext(),
                        it,
                        R.drawable.empty_profile_picture
                    )
                    profilePictureUri = it
                }
            }
        }

    override fun onFileAccessGranted() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        selectMediaLauncher.launch(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initActionButtonStart()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initActionButtonStart() {
        actionButton?.setOnClickListener {
            val email = emailField?.text?.toString()
            if (email?.isNotEmpty() == true) {
                authViewModel.checkEmail(
                    email,
                    onExistsUi = ::initSignInMode,
                    onUserDoesNotExistUi = ::initRegisterMode
                )
            }
        }
    }

    private fun initSignInMode() {
        passwordField?.visibility = View.VISIBLE
        displayNameField?.visibility = View.GONE
        actionButton?.text = "Sign In"
        actionButton?.setOnClickListener {
            actionButton?.showProgress()
            val email = emailField?.text?.toString()
            val password = passwordField?.text?.toString()
            if (email?.isNotEmpty() == true && password?.isNotEmpty() == true) {
                authViewModel.signInWithEmailPassword(email, password, onFinishUi = {
                    toApp()
                })
            } else {
                actionButton?.stopProgress()
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRegisterMode() {
        passwordField?.visibility = View.VISIBLE
        displayNameField?.visibility = View.VISIBLE
        profilePicture?.visibility = View.VISIBLE
        profilePicture?.setOnClickListener {
            requestFileAccess()
        }
        actionButton?.text = "Sign Up"
        actionButton?.setOnClickListener {
            actionButton?.showProgress()
            val email = emailField?.text?.toString()
            val password = passwordField?.text?.toString()
            val displayName = displayNameField?.text?.toString()

            if (email?.isNotEmpty() == true && password?.isNotEmpty() == true && password.length > 6 && displayName?.isNotEmpty() == true) {
                authViewModel.signUpWithEmailPassword(
                    email,
                    password,
                    displayName,
                    profilePictureUri,
                    onFinishUi = {
                        toApp()
                    })
            } else {
                Toast.makeText(
                    requireContext(),
                    "Password needs 6 or more letters",
                    Toast.LENGTH_SHORT
                ).show()
                actionButton?.stopProgress()
            }
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
        fun newInstance() =
            EmailFragment()
    }
}