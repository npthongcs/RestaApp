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
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.viewmodel.HomeViewModel
import java.util.*


class SelectTimeFragment : Fragment(R.layout.fragment_select_time) {

    lateinit var dateBooking: ImageView
    lateinit var timeBooking: ImageView
    lateinit var tvDateBook: TextView
    lateinit var tvTimeBook: TextView
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_time, container, false)

        homeViewModel = (activity as HomeActivity).getHomeViewModel()

        dateBooking = view.findViewById(R.id.date_booking)
        timeBooking = view.findViewById(R.id.time_booking)
        tvDateBook = view.findViewById(R.id.tv_date_book)
        tvTimeBook = view.findViewById(R.id.tv_time_book)

        val c = Calendar.getInstance()
        var y = c.get(Calendar.YEAR)
        var m = c.get(Calendar.MONTH)
        var d = c.get(Calendar.DAY_OF_MONTH)

        val date = homeViewModel.getDateBook()

        if (date == "") tvDateBook.text = "${d}/${m+1}/${y}"
        else tvDateBook.text = date

        dateBooking.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, dayOfMonth ->
                    y = year
                    m = month
                    d = dayOfMonth
                    tvDateBook.text = "${d}/${m+1}/${y}"
                    homeViewModel.setDateBook("${d}/${m+1}/${y}")
                }, y, m, d)
            }?.show()
        }

        if (homeViewModel.getTimeBook() != "") tvTimeBook.text = homeViewModel.getTimeBook()

        timeBooking.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                c.set(Calendar.MINUTE,minute)
                var minu = ""
                if (minute<10) minu = "0$minute" else minu = "$minute"
                tvTimeBook.text = "${hourOfDay}:${minu}"
                homeViewModel.setTimeBook("${hourOfDay}:${minu}")
            }
            TimePickerDialog(context,timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }
        return view
    }
}