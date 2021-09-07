package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.databinding.FragmentDetailFoodBinding
import com.anderson.restaapp.databinding.FragmentSelectFoodBinding
import com.anderson.restaapp.model.FoodSelected
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFoodFragment : Fragment() {

    private var _binding: FragmentDetailFoodBinding? = null
    private val args: DetailFoodFragmentArgs by navArgs()
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var count = 1
    private var listBooking = ArrayList<FoodSelected>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = HomeActivity.homeViewModel
        _binding = FragmentDetailFoodBinding.inflate(inflater,container,false)
        val view = binding.root
        Glide.with(binding.idImageFood.context).load(args.data.url).into(binding.idImageFood)
        binding.idPriceFood.text = "$"+args.data.price.toString()
        binding.idNameFood.text = args.data.name
        binding.idDescription.text = args.data.description
        binding.idAmount.text = count.toString()

        binding.idIncrease.setOnClickListener {
            count++
            binding.idAmount.text = count.toString()
        }

        binding.idDecrease.setOnClickListener {
            if (count>1){
                count--
                binding.idAmount.text = count.toString()
            }
        }

        listBooking = homeViewModel.getListBooking()

        binding.idButtonAdd.setOnClickListener {
            if (listBooking.size == 0) {
                val foodSelected = FoodSelected(args.data.name,count,count*args.data.price)
                listBooking.add(foodSelected)
            } else {
                var isOK = false
                for (i in listBooking) {
                    if (i.name == args.data.name) {
                        i.amountFood += count
                        i.payment += count * args.data.price
                        isOK = true
                        break
                    }
                }
                if (!isOK){
                    val foodSelected = FoodSelected(args.data.name, count, count * args.data.price)
                    listBooking.add(foodSelected)
                }
            }
            Toast.makeText(context,"Add ${args.data.name} successfully",Toast.LENGTH_SHORT).show()
        }

        setTitleToolbar("Food detail")

        return view
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
}