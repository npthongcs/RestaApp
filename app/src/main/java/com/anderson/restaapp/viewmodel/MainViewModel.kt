package com.anderson.restaapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anderson.restaapp.repository.MainRepository
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {
    private val mainRepository = MainRepository()

    //sign up
    fun signupAccount(email: String, password: String, username: String){
        mainRepository.processSignup(email, password, username)
    }
    fun getSignupLiveDataObserver(): MutableLiveData<String>{
        return mainRepository.signupLiveDataObserver()
    }

    //login
    fun loginEmail(email: String, password: String){
        mainRepository.processLogin(email, password)
    }
    fun getLoginLiveDataObserver(): MutableLiveData<String>{
        return mainRepository.loginLiveDataObserver()
    }
}