package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.activity.MainActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.databinding.FragmentLoginEmailBinding
import com.anderson.restaapp.databinding.FragmentSelectFoodBinding
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.anderson.restaapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SelectFoodFragment : Fragment() {

    private var _binding: FragmentSelectFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var database: DatabaseReference
    private lateinit var foodAdapter: FoodAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var listFood: ArrayList<ItemFood>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentSelectFoodBinding.inflate(inflater,container,false)
        val view = binding.root

        database = Firebase.database.reference
        listFood = homeViewModel.getListFood()
        foodAdapter = FoodAdapter(listFood)

        makeObserver()
        setupRecyclerview()
        setupHideBotNav()
        homeViewModel.fetchListFood()

//        val itemFood = ItemFood("Chicken",12.10,"","ngon",0.0)
//
//        binding.tagFood.setOnClickListener {
//            database.child("Foods").push().setValue(itemFood)
//        }

        return view
    }

    private fun setupHideBotNav() {
        val botNav = (activity as HomeActivity).findViewById<BottomNavigationView>(R.id.bot_nav)
        binding.rvFood.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0 && botNav.isShown) botNav.visibility = View.GONE
                else if (dy<0) botNav.visibility = View.VISIBLE
            }
        })
    }

    private fun setupRecyclerview() {
        layoutManager = GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false)
        binding.rvFood.apply {
            setHasFixedSize(true)
            layoutManager = this@SelectFoodFragment.layoutManager
            adapter = foodAdapter
        }
    }

    private fun makeObserver() {
        homeViewModel.getFoodLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null){
                listFood.add(it)
                homeViewModel.setListFood(listFood)
                if (listFood.size == 1) foodAdapter.notifyDataSetChanged()
                else foodAdapter.notifyItemInserted(listFood.size)
            }
        })

        homeViewModel.getStatusFoodLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null){
                Log.d("message",it)
                val message = it.split(':')
                val status = message[0]
                val pos = message[1].toInt()
                when (status) {
                    "add" -> foodAdapter.notifyItemInserted(listFood.size-1)
                }
            }
        })
    }
}