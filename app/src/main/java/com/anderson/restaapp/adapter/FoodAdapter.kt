package com.anderson.restaapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anderson.restaapp.`interface`.ClickItemFood
import com.anderson.restaapp.databinding.ItemBinding
import com.anderson.restaapp.model.ItemFood
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class FoodAdapter (private val lFood: ArrayList<ItemFood>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickItemFood: ClickItemFood? = null

    fun setOnCallbackListener(clickItemFood: ClickItemFood){
        this.clickItemFood = clickItemFood
    }

    inner class ViewHolder (private val binding: ItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: ItemFood){
            binding.data = data
            binding.idPrice.text = "$"+data.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemBinding.inflate(inflater,parent,false)).apply {
            this@FoodAdapter.clickItemFood?.onClickItemFood(lFood[bindingAdapterPosition])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(lFood[position])
    }

    override fun getItemCount(): Int {
        return lFood.size
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(imageID: ImageView, url: String){
            Glide.with(imageID.context)
                .load(url)
                .override(280,280)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageID)
        }
    }

}