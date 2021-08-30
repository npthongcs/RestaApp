package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.databinding.FragmentSelectDessertBinding
import com.anderson.restaapp.databinding.FragmentSelectDinkBinding
import com.anderson.restaapp.listener.ClickItemFood
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SelectDessertFragment : BaseFragment(), ClickItemFood {

    private var _binding: FragmentSelectDessertBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var layoutManager: GridLayoutManager
    private var listDessert = ArrayList<ItemFood>()
    private var filterDessert = ArrayList<ItemFood>()
    private var tempList = ArrayList<ItemFood>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentSelectDessertBinding.inflate(inflater,container,false)
        val view = binding.root

        makeObserver()

        listDessert = homeViewModel.getListDessert()
        foodAdapter = FoodAdapter(listDessert)

        if (listDessert.size == 0) homeViewModel.fetchListDessert()

        filterDessert.clear()
        filterDessert.addAll(listDessert)

        foodAdapter.setOnCallbackListener(this)

        setupRecyclerview()
        performSearch()
        setupHideBotNav()
        return view
    }

    private fun setupHideBotNav() {
        val botNav = (activity as HomeActivity).findViewById<BottomNavigationView>(R.id.bot_nav)
        binding.rvDessert.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0 && botNav.isShown) botNav.visibility = View.GONE
                else if (dy<0) botNav.visibility = View.VISIBLE
            }
        })
    }

    private fun performSearch() {
        binding.searchDessert.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
            listDessert.clear()
            listDessert.addAll(filterDessert)
            listDessert.forEach { food ->
                if (food.name.contains(text,true)) tempList.add(food)
            }
            updateRecyclerView()
        }
    }

    private fun updateRecyclerView() {
        listDessert.clear()
        listDessert.addAll(tempList)
        foodAdapter.notifyDataSetChanged()
        (binding.rvDessert.layoutManager as GridLayoutManager).scrollToPosition(0)
    }

    private fun setupRecyclerview() {
        layoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)
        binding.rvDessert.apply {
            setHasFixedSize(true)
            layoutManager = this@SelectDessertFragment.layoutManager
            adapter = foodAdapter
        }
    }

    private fun makeObserver() {
        homeViewModel.getDessertLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null && filterDessert.size<homeViewModel.getKeysDessertSize()){
                listDessert.add(it)
                filterDessert.add(it)
                if (filterDessert.size == 1) foodAdapter.notifyDataSetChanged()
                else foodAdapter.notifyItemInserted(listDessert.size)
            }
        })

        homeViewModel.getStatusDessertLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null){
                val message = it.split('\t')
                val status = message[0]
                val pos = message[1].toInt()
                when (status) {
                    "change" -> {
                        val item = ItemFood(message[2],message[3].toDouble(),message[4],message[5],message[6].toDouble())
                        listDessert[pos] = item
                        filterDessert[pos] = item
                        foodAdapter.notifyItemChanged(pos)
                    }
                    "remove" -> {
                        listDessert.removeAt(pos)
                        filterDessert.removeAt(pos)
                        foodAdapter.notifyItemRemoved(pos)
                    }
                }
            }
        })
    }

    override fun onClickItemFood(data: ItemFood) {
        val action = SelectDessertFragmentDirections.actionSelectDessertFragmentToDetailFoodFragment(data)
        findNavController().navigate(action)
    }

    override fun onPause() {
        listDessert.clear()
        listDessert.addAll(filterDessert)
        super.onPause()
    }
}