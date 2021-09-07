package com.anderson.restaapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    lateinit var googleSignInClient: GoogleSignInClient
    private val auth = FirebaseAuth.getInstance()
    val WEB_CLIENT_ID = "949654097701-po1qrabsvgpmu6ql78vmciuptcm0qosk.apps.googleusercontent.com"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val btnLoginEmail = view.findViewById<ImageView>(R.id.btnLoginEmail)
        val btnLoginGG = view.findViewById<ImageView>(R.id.btnLoginGoogle)
        val btnSignup = view.findViewById<TextView>(R.id.btnSignup)

        configGoogleSignIn()

        btnLoginEmail.setOnClickListener {
            replaceFragment(LoginEmailFragment())
        }
        btnLoginGG.setOnClickListener {
            resultLauncher.launch(Intent(googleSignInClient.signInIntent))
        }
        btnSignup.setOnClickListener {
            replaceFragment(SignupFragment())
        }
        return view
    }

    private fun configGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = activity?.let { GoogleSignIn.getClient(it,gso) }!!
    }

    private val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = it.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("signInWithCredential:failure", it.exception.toString())
                }
            }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

}