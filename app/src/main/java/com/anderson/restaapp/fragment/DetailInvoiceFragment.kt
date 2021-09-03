package com.anderson.restaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class DetailInvoiceFragment : BaseFragment() {

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
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentDetailInvoiceBinding.inflate(inflater, container, false)
        val view = binding.root

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

}