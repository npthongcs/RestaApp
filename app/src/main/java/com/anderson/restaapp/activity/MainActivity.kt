package com.anderson.restaapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.anderson.restaapp.R
import com.anderson.restaapp.fragment.LoginFragment
import com.anderson.restaapp.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    companion object{
        var mainViewModel = MainViewModel()
    }

    fun getMainViewModel(): MainViewModel{
        return mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val auth = Firebase.auth
        if (auth.currentUser!=null){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, LoginFragment())
            .addToBackStack(null)
            .commit()
    }
}