package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.databinding.FragmentDetailFoodBinding
import com.anderson.restaapp.databinding.FragmentSelectFoodBinding
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.bumptech.glide.Glide

class DetailFoodFragment : BaseFragment() {

    private var _binding: FragmentDetailFoodBinding? = null
    private val args: DetailFoodFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentDetailFoodBinding.inflate(inflater,container,false)
        val view = binding.root
        Glide.with(binding.idImageFood.context).load(args.data.url).into(binding.idImageFood)
        binding.idPriceFood.text = "$"+args.data.price.toString()
        binding.idNameFood.text = args.data.name
        binding.idDescription.text = args.data.description
        return view
    }
}