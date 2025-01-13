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
            val user = UserModel.fromFirebaseAuth()
            usersRepository.getUserFromRemoteSource(user.uid).run {
                if (this == null) {
                    usersRepository.upsertUser(user)
                }
            }
            onFinishUi()
        }
    }
}