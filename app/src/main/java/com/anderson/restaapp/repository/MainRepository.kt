package com.anderson.restaapp.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainRepository {

    var auth = FirebaseAuth.getInstance()
    var signupLiveData = MutableLiveData<String>()
    var loginLiveData = MutableLiveData<String>()

    // sign up
    fun processSignup(email: String, password: String, username: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val profileUpdate = userProfileChangeRequest { displayName = username }
                    val user = Firebase.auth.currentUser
                    user!!.updateProfile(profileUpdate)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("signup","success")
                                signupLiveData.postValue("ok")
                            }
                        }
                } else {
                    val message = it.exception.toString().split(':')
                    Log.d("sign up failed", message[1])
                    signupLiveData.postValue(message[1])
                }
            }
    }
    fun signupLiveDataObserver(): MutableLiveData<String>{
        return signupLiveData
    }

    // login
    fun processLogin(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("login","successful")
                    loginLiveData.postValue("ok")
                } else {
                    val message = it.exception.toString().split(':')[1]
                    if (message.length>65) loginLiveData.postValue("Email is invalid")
                    else loginLiveData.postValue("Password is invalid")
                    Log.d("login failed",message)
                }
            }
    }
    fun loginLiveDataObserver():MutableLiveData<String>{
        return loginLiveData
    }
}