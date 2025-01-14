package com.example.concertio.ui.main.fragments.edit_profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.concertio.R
import com.example.concertio.extensions.FileUploadingFragment
import com.example.concertio.extensions.loadProfilePicture
import com.example.concertio.extensions.showProgress
import com.example.concertio.ui.main.UserProfileViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class EditProfileFragment : FileUploadingFragment() {
    private val profilePictureView by lazy { view?.findViewById<ImageView>(R.id.user_profile_picture) }
    private val nameField by lazy { view?.findViewById<EditText>(R.id.edit_profile_name) }
    private val emailField by lazy { view?.findViewById<EditText>(R.id.edit_profile_email) }
    private val passwordField by lazy { view?.findViewById<EditText>(R.id.edit_profile_password) }
    private val saveButton by lazy { view?.findViewById<MaterialButton>(R.id.save_profile_changes) }

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private lateinit var profilePictureUri: Uri
    private val selectMediaLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    profilePictureView?.loadProfilePicture(
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFields(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initFields(view: View) {
        profilePictureView?.setOnClickListener {
            requestFileAccess()
        }
        saveButton?.setOnClickListener {
            saveButton?.showProgress(resources.getColor(com.firebase.ui.auth.R.color.colorPrimary))
            userProfileViewModel.updateProfile(
                nameField?.text.toString(),
                emailField?.text.toString(),
                passwordField?.text.toString(),
                profilePictureUri
            ) {
                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToUserProfileFragment())
            }
        }
        userProfileViewModel.observeMyProfile().observe(viewLifecycleOwner) {
            it?.run {
                nameField?.setText(name)
                emailField?.setText(email)
                profilePicture?.run {
                    profilePictureUri = Uri.parse(this)
                    profilePictureView?.loadProfilePicture(
                        view.context,
                        profilePictureUri,
                        R.drawable.empty_profile_picture
                    )
                }
            }
        }
    }
}