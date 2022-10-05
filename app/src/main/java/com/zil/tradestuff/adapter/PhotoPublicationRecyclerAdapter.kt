package com.zil.tradestuff.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zil.tradestuff.R

class PhotoPublicationRecyclerAdapter(val photos: List<Uri>)  : RecyclerView.Adapter<PhotoPublicationRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bind(uri : Uri){
            val photo = itemView.findViewById<ImageView>(R.id.image_photo)
            Glide.with(itemView.context).load(uri).into(photo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board_of_thing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}