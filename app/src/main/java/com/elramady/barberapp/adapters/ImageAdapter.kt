package com.elramady.barberapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.barberapp.R
import com.elramady.barberapp.databinding.ImageItemBinding
import com.elramady.barberapp.models.Images
import com.squareup.picasso.Picasso

class ImageAdapter (val context: Context): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var imageList= ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : ImageItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.image_item,parent,false)
        return ImageViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image: String =imageList[position]
        holder.bind(image,context)

    }

    override fun getItemCount(): Int {
        return imageList.size
    }
    fun setList(list: ArrayList<String>) {
        this.imageList= list
        notifyDataSetChanged()

    }


    inner class ImageViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(image: String, context: Context) {
            Picasso.get().load(image).into(binding.imageItem)

        }

    }
}