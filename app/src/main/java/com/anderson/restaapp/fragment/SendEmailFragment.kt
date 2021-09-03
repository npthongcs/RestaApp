package com.anderson.restaapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anderson.restaapp.R
import com.anderson.restaapp.databinding.FragmentSendEmailBinding
import com.anderson.restaapp.databinding.FragmentViewReviewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SendEmailFragment : BaseFragment() {

    private var _binding: FragmentSendEmailBinding? = null
    private val binding get() = _binding!!
    private val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSendEmailBinding.inflate(inflater, container, false)
        val view = binding.root

        if (user != null) {
            binding.emailFrom.text = user.email
        }

        binding.btnSendMail.setOnClickListener {
            val content = binding.contentMail.text.toString()
            val email = "resta.contact.sing@gmail.com"
            val address = email.split(",").toTypedArray()
            val intent = Intent(Intent.ACTION_SEND)
            intent.data = Uri.parse("mailto:")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL,address)
            intent.putExtra(Intent.EXTRA_TEXT,content)
            startActivity(Intent.createChooser(intent, "Select email"))
        }

        return view
    }

}