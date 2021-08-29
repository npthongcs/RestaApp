package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.listener.ClickItemFood
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.databinding.FragmentSelectFoodBinding
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SelectFoodFragment : BaseFragment(), ClickItemFood {

    private var _binding: FragmentSelectFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var database: DatabaseReference
    private lateinit var foodAdapter: FoodAdapter
    lateinit var layoutManager: GridLayoutManager
    private lateinit var listFood: ArrayList<ItemFood>
    private var filterFood = ArrayList<ItemFood>()
    private var tempList = ArrayList<ItemFood>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentSelectFoodBinding.inflate(inflater,container,false)
        val view = binding.root

        makeObserver()

        database = Firebase.database.reference
        listFood = homeViewModel.getListFood()
        foodAdapter = FoodAdapter(listFood)

        if (listFood.size == 0) homeViewModel.fetchListFood()

        filterFood.clear()
        filterFood.addAll(listFood)

        foodAdapter.setOnCallbackListener(this)
        setupRecyclerview()
        performSearch()
        setupHideBotNav()

        return view
    }

    private fun performSearch() {
        binding.searchFood.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return false
            }

        })
    }

    private fun search(text: String?) {
        text?.let {
            tempList.clear()
            listFood.clear()
            listFood.addAll(filterFood)
            listFood.forEach { food ->
                if (food.name.contains(text,true)) tempList.add(food)
            }
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        listFood.clear()
        listFood.addAll(tempList)
        binding.rvFood.adapter?.notifyDataSetChanged()
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
            if (it!=null && filterFood.size<homeViewModel.getKeysFoodSize()){
                listFood.add(it)
                filterFood.add(it)
                if (filterFood.size == 1) foodAdapter.notifyDataSetChanged()
                else foodAdapter.notifyItemInserted(filterFood.size)
            }
        })

        homeViewModel.getStatusFoodLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null){
                val message = it.split('\t')
                val status = message[0]
                val pos = message[1].toInt()
                when (status) {
                    "change" -> {
                        val item = ItemFood(message[2],message[3].toDouble(),message[4],message[5],message[6].toDouble())
                        listFood[pos] = item
                        filterFood[pos] = item
                        foodAdapter.notifyItemChanged(pos)
                    }
                    "remove" -> {
                        listFood.removeAt(pos)
                        filterFood.removeAt(pos)
                        foodAdapter.notifyItemRemoved(pos)
                    }
                }
            }
        })
    }

    override fun onClickItemFood(data: ItemFood) {
        val action = SelectFoodFragmentDirections.actionSelectFoodFragmentToDetailFoodFragment(data)
        findNavController().navigate(action)
    }

    override fun onPause() {
        super.onPause()
        //homeViewModel.setPositionFood((binding.rvFood.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition())
        listFood.clear()
        listFood.addAll(filterFood)
    }

//    override fun onResume() {
//        super.onResume()
//        (binding.rvFood.layoutManager as LinearLayoutManager).scrollToPosition(homeViewModel.getPositionFood())
//    }
}


//        val itemFood = ItemFood("Chicken",12.10,"","ngon",0.0)
//
//        binding.tagFood.setOnClickListener {
//            database.child("Foods").push().setValue(itemFood)
//        }
