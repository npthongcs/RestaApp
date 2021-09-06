package com.anderson.restaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.databinding.ItemDetailInvoiceBinding
import com.anderson.restaapp.databinding.ItemInvoiceBinding
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.model.FoodSelected

class DetailInvoiceAdapter(private val lFood: ArrayList<FoodSelected>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDetailInvoiceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: FoodSelected){
            binding.nameDI.text = data.name
            binding.numberDI.text = data.amountFood.toString()
            binding.moneyDI.text = "$${data.payment}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemDetailInvoiceBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lFood[position])
    }

    override fun getItemCount(): Int {
        return lFood.size
    }

}