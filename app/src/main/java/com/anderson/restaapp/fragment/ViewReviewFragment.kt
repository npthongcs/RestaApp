package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.adapter.ReviewAdapter
import com.anderson.restaapp.databinding.FragmentViewReviewBinding
import com.anderson.restaapp.databinding.FragmentWriteReviewBinding
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.model.Rating
import com.anderson.restaapp.model.Review
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ViewReviewFragment : Fragment() {

    private var _binding: FragmentViewReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var listReview = ArrayList<Review>()
    private val database = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentViewReviewBinding.inflate(inflater, container, false)
        val view = binding.root

        makeObserver()
        listReview = homeViewModel.getListReview()
        reviewAdapter = ReviewAdapter(listReview)
        setupRecyclerView()

        if (listReview.size == 0) homeViewModel.getReview()

        binding.writeReview.setOnClickListener {
            val action = ViewReviewFragmentDirections.actionViewReviewFragmentToWriteReviewFragment()
            findNavController().navigate(action)
        }

        viewAvgRating()

        return view
    }

    private fun viewAvgRating() {
        database.child("Rating").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Rating::class.java)
                binding.apply {
                    if (data != null) {
                        avgRating.text = data.rating.toString()
                        numReview.text = data.number.toString()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("get rating","failed")
            }

        })
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
        binding.rvReview.apply {
            setHasFixedSize(true)
            layoutManager = this@ViewReviewFragment.layoutManager
            adapter = reviewAdapter
        }
    }

    private fun makeObserver() {
        homeViewModel.getReviewLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null && listReview.size<homeViewModel.getKeysReviewSize()){
                listReview.add(it)
                if (listReview.size == 1) reviewAdapter.notifyDataSetChanged()
                else reviewAdapter.notifyItemInserted(listReview.size)
            }
        })
    }
}