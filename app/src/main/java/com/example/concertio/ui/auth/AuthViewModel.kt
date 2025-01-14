package com.example.concertio.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val usersRepository = UsersRepository.getInstance()
    fun register(onFinishUi: () -> Unit) {
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
}