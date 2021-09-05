package com.anderson.restaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.databinding.ItemInvoiceBinding
import com.anderson.restaapp.databinding.ItemReviewBinding
import com.anderson.restaapp.model.DetailBooking
import com.anderson.restaapp.model.Review
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ReviewAdapter(val lReview: ArrayList<Review>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Review){
            binding.reviewName.text = data.name
            binding.contentReview.text = data.content
            binding.timeReview.text = data.time
            binding.ratingBar.rating = data.star.toFloat()
            Glide.with(binding.avatarReview.context).load(data.urlAvatar).into(binding.avatarReview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemReviewBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lReview[position])
    }

    override fun getItemCount(): Int {
        return lReview.size
    }

}