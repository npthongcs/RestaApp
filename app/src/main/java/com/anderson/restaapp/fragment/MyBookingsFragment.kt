package com.anderson.restaapp.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.adapter.InvoiceAdapter
import com.anderson.restaapp.databinding.FragmentDetailBookingBinding
import com.anderson.restaapp.databinding.FragmentMyBookingsBinding
import com.anderson.restaapp.databinding.FragmentSelectDinkBinding
import com.anderson.restaapp.listener.ClickItemInvoice
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MyBookingsFragment : Fragment(), ClickItemInvoice {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var invoiceAdapter: InvoiceAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var listInvoice = ArrayList<DetailBooking>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        val view = binding.root

        setTitleToolbar("My bookings")

        makeObserver()
        listInvoice = homeViewModel.getListInvoice()
        invoiceAdapter = InvoiceAdapter(listInvoice)
        invoiceAdapter.setOnCallbackListener(this)
        setupRecyclerView()

        if (listInvoice.size == 0) homeViewModel.getInvoice()

        return view
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        binding.rvInvoice.apply {
            setHasFixedSize(true)
            layoutManager = this@MyBookingsFragment.layoutManager
            adapter = invoiceAdapter
        }
    }

    private fun makeObserver() {
        homeViewModel.getInvoiceLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null && listInvoice.size<homeViewModel.getKeysInvoiceSize()){
                listInvoice.add(it)
                if (listInvoice.size == 1) invoiceAdapter.notifyDataSetChanged()
                else invoiceAdapter.notifyItemInserted(listInvoice.size)
            }
        })
    }

    override fun onClickItemInvoice(data: DetailBooking) {
        val action = MyBookingsFragmentDirections.actionMyBookingsFragmentToDetailInvoiceFragment(data)
        findNavController().navigate(action)
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