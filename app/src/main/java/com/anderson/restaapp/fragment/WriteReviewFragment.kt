package com.anderson.restaapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.databinding.FragmentDetailInvoiceBinding
import com.anderson.restaapp.databinding.FragmentWriteReviewBinding
import com.anderson.restaapp.model.Rating
import com.anderson.restaapp.model.Review
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class WriteReviewFragment : Fragment() {

    private var _binding: FragmentWriteReviewBinding? = null
    private val binding get() = _binding!!
    private val user = Firebase.auth.currentUser
    private val database = Firebase.database.reference
    private var star = 0
    private var content = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWriteReviewBinding.inflate(inflater, container, false)
        val view = binding.root

        setTitleToolbar("Review")

        binding.btnSendReview.setOnClickListener {
            star = binding.rating.rating.toInt()
            content = binding.etContentReview.text.toString()
            calculateRating()
            uploadDatabase()
        }

        return view
    }

    private fun uploadDatabase() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ssa")
        val currentDate = sdf.format(Date())
        var name = user?.displayName
        if (name==null) name = user?.email
        val review = name?.let { Review(it,star,currentDate,content,user?.photoUrl.toString()) }
        database.child("Review").push().setValue(review).addOnCompleteListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Thanks for reviewing our restaurant.")
            builder.setPositiveButton("OK") { _, _ ->
                val action = WriteReviewFragmentDirections.actionWriteReviewFragmentToViewReviewFragment()
                findNavController().navigate(action)
            }
            builder.show()
        }
    }

    private fun calculateRating() {
        database.child("Rating").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Rating::class.java)
                if (data!=null){
                    var k = data.number * data.rating
                    k += star
                    k /= (data.number+1)
                    val rating = Rating(data.number+1,k)
                    updateRating(rating)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("get rating","failed")
            }

        })
    }

    private fun updateRating(rating: Rating) {
        database.child("Rating").setValue(rating)
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