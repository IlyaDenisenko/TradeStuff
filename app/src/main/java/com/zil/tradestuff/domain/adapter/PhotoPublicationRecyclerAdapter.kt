package com.zil.tradestuff.domain.adapter

import android.net.Uri
import android.util.Log
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
            val photo = itemView.findViewById<ImageView>(R.id.description_photo_recycler_view)
            Glide.with(itemView.context).load(uri).into(photo)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_description_thing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}