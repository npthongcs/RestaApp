package com.anderson.restaapp.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val user = Firebase.auth.currentUser

    private var mUri: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        setTitleToolbar("My profile")

        binding.apply {
            if (user != null) {
                profileDisplayName.setText(user.displayName)
                profileEmail.text = user.email
                if (user.photoUrl != null)
                    Glide.with(avatar.context).load(user.photoUrl).into(avatar)
            }
            btnSaveprofile.setOnClickListener {
                if (mUri!=null) uploadImage()
                val profileUpdate = userProfileChangeRequest {
                    displayName = profileDisplayName.text.toString()
                }
                user?.updateProfile(profileUpdate)?.addOnCompleteListener {
                    if (it.isSuccessful) Toast.makeText(context,"Update profile successfully",Toast.LENGTH_SHORT).show()
                }
            }
            avatar.setOnClickListener {
                onClickRequestPermission()
            }
        }

        return view
    }

    private fun uploadImage() {
        mUri?.let {
            val ref = storageReference?.child("uploads/"+UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(mUri!!)

            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener {
                if (it.isSuccessful) {
                    updateInforUser(it.result)
                }
            }
        }
    }

    private fun updateInforUser(downloadUri: Uri) {
        val profileUpdate = userProfileChangeRequest {
            photoUri = downloadUri
        }
        user?.updateProfile(profileUpdate)
    }

    private fun onClickRequestPermission() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED) {
            launchGallery()
        } else {
            requestMultiplePermissions.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                launchGallery()
            }
        }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        mActivityResultLauncher.launch(Intent.createChooser(intent,"Select picture"))
    }

    private val mActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK){
            val data: Intent = it.data ?: return@registerForActivityResult
            val uri: Uri? = data.data
            try {
                uri?.let {
                    if (Build.VERSION.SDK_INT<28){
                        val bitmap = getBitmap(
                            activity?.contentResolver,
                            uri
                        )
                        binding.avatar.setImageBitmap(bitmap)
                        mUri = uri
                    } else {
                        val source = activity?.contentResolver?.let { it1 ->
                            ImageDecoder.createSource(
                                it1,
                                uri)
                        }
                        val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                        binding.avatar.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setTitleToolbar(title: String) {
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = title
    }

    override fun onResume() {
        super.onResume()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bot_nav)
        botBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bot_nav)
        botBar.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
}