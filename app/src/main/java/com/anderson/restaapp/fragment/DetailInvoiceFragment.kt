package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.DetailInvoiceAdapter
import com.anderson.restaapp.adapter.InvoiceAdapter
import com.anderson.restaapp.databinding.FragmentDetailInvoiceBinding
import com.anderson.restaapp.databinding.FragmentMyBookingsBinding
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailInvoiceFragment : Fragment() {

    private val args: DetailInvoiceFragmentArgs by navArgs()
    private var _binding: FragmentDetailInvoiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailInvoiceAdapter: DetailInvoiceAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = HomeActivity.homeViewModel
        _binding = FragmentDetailInvoiceBinding.inflate(inflater, container, false)
        val view = binding.root

        setTitleToolbar("Invoice detail")

        detailInvoiceAdapter = DetailInvoiceAdapter(args.data.listBook)
        setupRecyclerView()
        viewData()

        return view
    }

    private fun viewData() {
        binding.apply {
            numberPeople.text = args.data.amountPeople.toString()
            payment.text = "$"+args.data.totalPayment.toString()
            detailInvoiceDiscount.text = args.data.discount.toString()+"%"
            note.text = args.data.note
            Log.d("qrcode link", args.data.urlQRCode)
            Glide.with(idQRCode.context).load(args.data.urlQRCode).into(idQRCode)
        }
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        binding.rvDetailInvoice.apply {
            setHasFixedSize(true)
            layoutManager = this@DetailInvoiceFragment.layoutManager
            adapter = detailInvoiceAdapter
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