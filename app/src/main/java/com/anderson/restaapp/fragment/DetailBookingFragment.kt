package com.anderson.restaapp.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.anderson.restaapp.R
import com.anderson.restaapp.activity.HomeActivity
import com.anderson.restaapp.adapter.FoodAdapter
import com.anderson.restaapp.adapter.FoodBookingAdapter
import com.anderson.restaapp.databinding.FragmentDetailBookingBinding
import com.anderson.restaapp.databinding.FragmentSelectDinkBinding
import com.anderson.restaapp.listener.ClickInDetailBooking
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.model.FoodSelected
import com.anderson.restaapp.model.ItemFood
import com.anderson.restaapp.model.Quantity
import com.anderson.restaapp.viewmodel.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailBookingFragment : BaseFragment(), ClickInDetailBooking {

    private var _binding: FragmentDetailBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var foodBookingAdapter: FoodBookingAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var listFoodSelected: ArrayList<FoodSelected>
    private var count = 0
    private var discount = 0.0
    private var money = 0.0
    private var totalPayment = 0.0
    private var amountPeople = 0
    private val user = Firebase.auth.currentUser
    private val database = Firebase.database.reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = (activity as HomeActivity).getHomeViewModel()
        _binding = FragmentDetailBookingBinding.inflate(inflater, container, false)
        val view = binding.root

        listFoodSelected = homeViewModel.getListBooking()
        foodBookingAdapter = FoodBookingAdapter(listFoodSelected)
        foodBookingAdapter.setOnCallbackListener(this)
        setupRecyclerview()
        foodBookingAdapter.notifyDataSetChanged()

        makeObserver()
        viewData()
        countFoodAndMoney()
        calculateDiscount()
        calculatePayment()
        initClick()

        return view
    }

    private fun makeObserver() {
        homeViewModel.getQuantityLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                homeViewModel.setQuantityAvailable(it)
                Log.d("quantity", it.toString())
                if (it.maxQuantity == -1) binding.idSlot.text = "--"
                else binding.idSlot.text = it.remaining.toString()
            }
        })
    }

    private fun initClick() {
        binding.btnSaveNote.setOnClickListener {
            homeViewModel.setNote(binding.etNote.text.toString())
        }

        binding.btnPayment.setOnClickListener {
            if (checkData()) confirmPayment()
        }
    }

    private fun updateRemaining() {
        val s = homeViewModel.getDateBook().replace("/", "")
        database.child("Calendar").child(s).setValue(
            Quantity(
                homeViewModel.getQuantityAvailable().maxQuantity,
                homeViewModel.getQuantityAvailable().remaining - amountPeople
            )
        )
    }

    private fun confirmPayment() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm payment")
        builder.setMessage("Are you sure pay $totalPayment?")
        builder.setPositiveButton("OK") { _, _ ->
            processPayment()
        }
        builder.setNegativeButton("Cancel") { _, _ ->
        }
        builder.show()
    }

    private fun processPayment() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ssa")
        val currentDate = sdf.format(Date())

        val note = binding.etNote.text.toString()
        val userID = user?.uid
        val invoice = DetailBooking(
            amountPeople,
            homeViewModel.getDateBook(),
            homeViewModel.getTimeBook(),
            note,
            count,
            totalPayment,
            discount,
            listFoodSelected,
            currentDate
        )
        if (userID != null) {
            database.child("Bookings").child(userID).push().setValue(invoice)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Booking is successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateRemaining()
                        // action to
                    } else {
                        Log.d("add booking failed", it.exception.toString())
                    }
                }
        }
    }

    private fun checkData(): Boolean {
        val slot = homeViewModel.getQuantityAvailable()
        amountPeople = binding.amountPeople.text.toString().toInt()
        if (slot.maxQuantity == -1) {
            Toast.makeText(
                context,
                "Maximum booking date is 14 days from the current time",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (slot.remaining == 0) {
            Toast.makeText(
                context,
                "The quantity available not enough, please choose another date",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (slot.remaining < amountPeople) {
            Toast.makeText(
                context,
                "The quantity available for this date has run out, please choose another date",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (amountPeople == 0) {
            Toast.makeText(
                context,
                "Number of people must be greater than 0",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (listFoodSelected.size == 0) {
            Toast.makeText(
                context,
                "Please select foods, drinks and desserts",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            val s = homeViewModel.getTimeBook().split(':')[0].toInt()
            if (s < 9 || s > 20) {
                Toast.makeText(
                    context,
                    "Booking time must be from 9:00 to 20:00",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }

    private fun calculatePayment() {
        val moneyDiscount = money * discount / 100.0
        totalPayment = money - moneyDiscount
        binding.apply {
            this.moneyDiscount.text = "$${moneyDiscount}"
            this.totalPayment.text = "$"+this@DetailBookingFragment.totalPayment.toString()
        }
    }

    private fun calculateDiscount() {
        val k = count / 10
        if (k > 5) {
            binding.discount.text = "25.0"
            discount = 25.0
        } else {
            binding.discount.text = (k * 5).toString()
            discount = k * 5.0
        }
    }

    private fun countFoodAndMoney() {
        count = 0
        money = 0.0
        for (i in listFoodSelected) {
            count += i.amountFood
            money += i.payment
        }
        binding.apply {
            totalFood.text = count.toString()
            totalMoney.text = "$${money}"
        }
    }

    private fun viewData() {
        binding.amountPeople.setText("0")
        binding.timeBooking.text = homeViewModel.getTimeBook()
        if (homeViewModel.getNote() != "") binding.etNote.setText(homeViewModel.getNote())

        val c = Calendar.getInstance()
        var y = c.get(Calendar.YEAR)
        var m = c.get(Calendar.MONTH)
        var d = c.get(Calendar.DAY_OF_MONTH)
        val date = homeViewModel.getDateBook()
        binding.dateBooking.apply {
            text = date
            setOnClickListener {
                context?.let { it1 ->
                    DatePickerDialog(it1, { _, year, month, dayOfMonth ->
                        y = year
                        m = month
                        d = dayOfMonth
                        this.text = "${d}/${m + 1}/${y}"
                        homeViewModel.setDateBook("${d}/${m + 1}/${y}")
                        homeViewModel.getQuantity("${d}${m + 1}${y}")
                    }, y, m, d)
                }?.show()
            }
        }

        binding.timeBooking.apply {
            text = homeViewModel.getTimeBook()
            setOnClickListener {
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    c.set(Calendar.MINUTE, minute)
                    var minu = ""
                    minu = if (minute < 10) "0$minute" else "$minute"
                    this.text = "${hourOfDay}:${minu}"
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
        }

    }


    private fun setupRecyclerview() {
        layoutManager = LinearLayoutManager(context)
        binding.rvFoodBooking.apply {
            layoutManager = this@DetailBookingFragment.layoutManager
            setHasFixedSize(true)
            adapter = foodBookingAdapter
        }
    }

    override fun onClickDecreaseAmount(position: Int) {
        if (listFoodSelected[position].amountFood > 1) {
            val k = listFoodSelected[position].payment / listFoodSelected[position].amountFood
            listFoodSelected[position].amountFood -= 1
            listFoodSelected[position].payment = k * listFoodSelected[position].amountFood
            foodBookingAdapter.notifyItemChanged(position)
            countFoodAndMoney()
            calculateDiscount()
            calculatePayment()
        }
    }

    override fun onCLickIncreaseAmount(position: Int) {
        val k = listFoodSelected[position].payment / listFoodSelected[position].amountFood
        listFoodSelected[position].amountFood += 1
        listFoodSelected[position].payment = k * listFoodSelected[position].amountFood
        foodBookingAdapter.notifyItemChanged(position)
        countFoodAndMoney()
        calculateDiscount()
        calculatePayment()
    }

    override fun onClickDeleteItem(position: Int) {
        listFoodSelected.removeAt(position)
        foodBookingAdapter.notifyDataSetChanged()
        countFoodAndMoney()
        calculateDiscount()
        calculatePayment()
    }

}