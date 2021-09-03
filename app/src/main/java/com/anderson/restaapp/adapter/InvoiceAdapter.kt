package com.anderson.restaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.databinding.ItemBookingBinding
import com.anderson.restaapp.databinding.ItemInvoiceBinding
import com.anderson.restaapp.listener.ClickInDetailBooking
import com.anderson.restaapp.listener.ClickItemInvoice
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.model.FoodSelected

class InvoiceAdapter(private val lInvoice: ArrayList<DetailBooking>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickItemInvoice: ClickItemInvoice? = null

    fun setOnCallbackListener(clickItemInvoice: ClickItemInvoice){
        this.clickItemInvoice = clickItemInvoice
    }

    inner class ViewHolder(private val binding: ItemInvoiceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DetailBooking){
            binding.ivDateBooking.text = data.date+"   "+data.time
            binding.ivDatePayment.text = data.dateTimePayment
            binding.ivTotalPayment.text = "$"+data.totalPayment.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemInvoiceBinding.inflate(inflater,parent,false)).apply {
            itemView.setOnClickListener {
                this@InvoiceAdapter.clickItemInvoice?.onClickItemInvoice(lInvoice[bindingAdapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lInvoice[position])
    }

    override fun getItemCount(): Int {
        return lInvoice.size
    }

}