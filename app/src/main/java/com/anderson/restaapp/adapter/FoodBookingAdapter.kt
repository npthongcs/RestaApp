package com.anderson.restaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.databinding.ItemBookingBinding
import com.anderson.restaapp.listener.ClickInDetailBooking
import com.anderson.restaapp.model.FoodSelected

class FoodBookingAdapter(private val lFood: ArrayList<FoodSelected>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var clickInDetailBooking: ClickInDetailBooking? = null

    fun setOnCallbackListener(clickInDetailBooking: ClickInDetailBooking){
        this.clickInDetailBooking = clickInDetailBooking
    }

    inner class ViewHolder(private val binding: ItemBookingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: FoodSelected, position: Int){
            binding.nameFoodBooking.text = data.name
            binding.amountBooking.text = data.amountFood.toString()
            binding.moneyFood.text = "$"+data.payment.toString()
            binding.btnDecrease.setOnClickListener {
                this@FoodBookingAdapter.clickInDetailBooking?.onClickDecreaseAmount(position)
            }
            binding.btnIncrease.setOnClickListener {
                this@FoodBookingAdapter.clickInDetailBooking?.onCLickIncreaseAmount(position)
            }
            binding.btnRemoveFood.setOnClickListener {
                this@FoodBookingAdapter.clickInDetailBooking?.onClickDeleteItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBookingBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lFood[position], position)
    }

    override fun getItemCount(): Int {
        return lFood.size
    }
}