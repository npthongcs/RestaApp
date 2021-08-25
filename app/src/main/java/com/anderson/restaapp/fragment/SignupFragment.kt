package com.anderson.restaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.activity.MainActivity
import com.anderson.restaapp.databinding.FragmentSignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : BaseFragment() {

    private var username: String = ""
    private var email: String = ""
    private var password: String = ""
    private var repassword: String = ""
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private var mainViewModel = MainActivity().getMainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.btnConfirmSignup.setOnClickListener {
            getInput()
            checkInput()
        }
        makeObserver()
        return view
    }

    private fun makeObserver() {
        mainViewModel.getSignupLiveDataObserver().observe(viewLifecycleOwner,{
            if (it=="ok"){
                val intent = Intent(context, HomeActivity::class.java)
                startActivity(intent)
                val user = Firebase.auth.currentUser
                if (user != null) {
                    Log.d("email user",user.displayName.toString())
                }
            } else if (it != null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getInput() {
        username = binding.etUsername.text.toString()
        email = binding.etEmailSignup.text.toString()
        password = binding.etPasswordSignup.text.toString()
        repassword = binding.etRepassSignup.text.toString()
        Log.d("input sign up", "$username $email $password $repassword")
    }

    private fun checkInput() {
        if (username == "") Toast.makeText(context,"Username is empty",Toast.LENGTH_LONG).show()
        else if (email == "") Toast.makeText(context,"Email is empty",Toast.LENGTH_LONG).show()
        else if (password == "") Toast.makeText(context,"Password is empty",Toast.LENGTH_LONG).show()
        else if (repassword == "") Toast.makeText(context,"Re-password is empty",Toast.LENGTH_LONG).show()
        else if (password!=repassword) Toast.makeText(context,"Password and re-password don't match",Toast.LENGTH_LONG).show()
        else if (password.length<6) Toast.makeText(context,"Password should be at least 6 characters",Toast.LENGTH_LONG).show()
        else makeSignup()
    }

    private fun makeSignup() {
        mainViewModel.signupAccount(email, password,username)
    }
}

