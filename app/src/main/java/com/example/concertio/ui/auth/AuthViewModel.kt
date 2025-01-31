package com.example.concertio.ui.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val usersRepository = UsersRepository.getInstance()
    private fun register(onFinishUi: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            FirebaseAuth.getInstance().currentUser?.let {
                usersRepository.upsertUser(
                    UserModel(
                        uid = it.uid,
                        name = it.displayName ?: "",
                        email = it.email,
                        profilePicture = it.photoUrl?.toString()
                            ?: usersRepository.getUserByUid(it.uid)?.profilePicture
                    )
                )
                onFinishUi()
            }
        }
    }

    fun checkEmail(email: String, onExistsUi: () -> Unit, onUserDoesNotExistUi: () -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            if (usersRepository.isUserExisting(email)) {
                onExistsUi()
            } else {
                onUserDoesNotExistUi()
            }
        }

    fun signInWithEmailPassword(
        email: String,
        password: String,
        onFinishUi: () -> Unit,
        onErrorUi: (message: String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .await().user?.let {
                        register(onFinishUi)
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onErrorUi("Sign in failed")
                }
            }
        }
    }

    fun signUpWithEmailPassword(
        email: String,
        password: String,
        displayName: String,
        profilePictureUri: Uri? = null,
        onFinishUi: () -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .await().apply {
                    user?.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(displayName)
                            .setPhotoUri(profilePictureUri).build()
                    )
                }
            signInWithEmailPassword(email, password, onFinishUi, onErrorUi = {})
        }
    }

    fun signInWithIdToken(
        idOption: GetGoogleIdOption,
        credentialManager: CredentialManager,
        context: Context,
        onFinishUi: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val credential = credentialManager.getCredential(
                request = GetCredentialRequest.Builder().addCredentialOption(idOption)
                    .build(),
                context = context
            ).credential
            when (credential) {
                is CustomCredential -> {
                    val idToken = GoogleIdTokenCredential.createFrom(credential.data)
                    FirebaseAuth.getInstance().signInWithCredential(
                        GoogleAuthProvider.getCredential(
                            idToken.idToken,
                            null
                        )
                    ).await()
                    register {
                        onFinishUi()
                    }
                }
            }
        }
    }
}