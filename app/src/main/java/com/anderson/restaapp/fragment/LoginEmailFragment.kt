package com.anderson.restaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.activity.MainActivity
import com.anderson.restaapp.databinding.FragmentLoginEmailBinding
import com.anderson.restaapp.databinding.FragmentSignupBinding
import com.anderson.restaapp.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginEmailFragment : Fragment() {

    private var _binding: FragmentLoginEmailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainViewModel = (activity as MainActivity).getMainViewModel()
        _binding = FragmentLoginEmailBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.btnConfirmLogin.setOnClickListener {
            makeLogin()
        }
        makeObserver()
        return view
    }

    private fun makeObserver() {
        mainViewModel.getLoginLiveDataObserver().observe(viewLifecycleOwner,{
            if (it=="ok"){
                val intent = Intent(context,HomeActivity::class.java)
                startActivity(intent)
                Log.d("fragment","login successful")
            } else if (it!=null){
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun makeLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email=="") Toast.makeText(context,"Email is empty",Toast.LENGTH_LONG).show()
        else if (password=="") Toast.makeText(context,"Password is empty",Toast.LENGTH_LONG).show()
        else mainViewModel.loginEmail(email,password)
    }


}