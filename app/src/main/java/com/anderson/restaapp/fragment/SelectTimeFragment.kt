package com.anderson.restaapp.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class SelectTimeFragment : Fragment() {

    lateinit var dateBooking: ImageView
    private lateinit var timeBooking: ImageView
    lateinit var tvDateBook: TextView
    lateinit var tvTimeBook: TextView
    private lateinit var homeViewModel: HomeViewModel
    var isView = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_time, container, false)

        homeViewModel = HomeActivity.homeViewModel
        makeObserver()
        setTitleToolbar("Home")

        dateBooking = view.findViewById(R.id.date_booking)
        timeBooking = view.findViewById(R.id.time_booking)
        tvDateBook = view.findViewById(R.id.tv_date_book)
        tvTimeBook = view.findViewById(R.id.tv_time_book)

        val sendMail = view.findViewById<ImageView>(R.id.sendmail)
        sendMail.setOnClickListener {
            val action = SelectTimeFragmentDirections.actionSelectTimeFragmentToSendEmailFragment()
            findNavController().navigate(action)
        }

        val c = Calendar.getInstance()
        var y = c.get(Calendar.YEAR)
        var m = c.get(Calendar.MONTH)
        var d = c.get(Calendar.DAY_OF_MONTH)

        val date = homeViewModel.getDateBook()

        if (date == "") {
            tvDateBook.text = "${d}/${m + 1}/${y}"
            homeViewModel.setDateBook("${d}/${m + 1}/${y}")
            val s = homeViewModel.getDateBook().replace("/", "")
            homeViewModel.getQuantity(s)
        } else tvDateBook.text = date

        dateBooking.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, dayOfMonth ->
                    y = year
                    m = month
                    d = dayOfMonth
                    tvDateBook.text = "${d}/${m + 1}/${y}"
                    homeViewModel.setDateBook("${d}/${m + 1}/${y}")
                    val s = homeViewModel.getDateBook().replace("/", "")
                    homeViewModel.getQuantity(s)
                    isView = true
                }, y, m, d)
            }?.show()
        }

        tvTimeBook.text = homeViewModel.getTimeBook()

        timeBooking.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                var minu = ""
                minu = if (minute < 10) "0$minute" else "$minute"
                tvTimeBook.text = "${hourOfDay}:${minu}"
                homeViewModel.setTimeBook("${hourOfDay}:${minu}")
            }
            TimePickerDialog(
                context,
                timeSetListener,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            ).show()
        }
        return view
    }

    private fun makeObserver() {
        homeViewModel.getQuantityLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                homeViewModel.setQuantityAvailable(it)
                if (isView) {
                    if (it.maxQuantity == -1) Toast.makeText(
                        context,
                        "Maximum booking date is 14 days from the current time",
                        Toast.LENGTH_SHORT
                    ).show()
                    else if (it.remaining == 0) Toast.makeText(
                        context,
                        "The quantity available for this date has run out, please choose another date",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isView = false
            }
        })
    }

    fun setTitleToolbar(title: String) {
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = title
    }
}