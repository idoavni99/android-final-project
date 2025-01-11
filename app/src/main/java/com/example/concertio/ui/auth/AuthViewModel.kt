package com.example.concertio.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertio.data.users.UserModel
import com.example.concertio.data.users.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val usersRepository = UsersRepository()
    fun register(onFinishUi: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            usersRepository.upsertUser(UserModel.fromFirebaseAuth())
            onFinishUi()
        }
    }
}