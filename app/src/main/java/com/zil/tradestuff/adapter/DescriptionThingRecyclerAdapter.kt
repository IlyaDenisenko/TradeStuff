package com.zil.tradestuff.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.zil.tradestuff.R
import com.zil.tradestuff.model.ThingModel

class DescriptionThingRecyclerAdapter(val thing: ThingModel, val id: Int):
    RecyclerView.Adapter<DescriptionThingRecyclerAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var photo: ImageView? = null
        init {
            photo = itemView.findViewById(R.id.description_photo_recycler_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_description_thing, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.i("Description Adapter: ", thing.toString())
        val listUri = thing.images
        var uri = listUri[position]
        Glide.with(holder.itemView.context)
            .load(uri)
            .override(SIZE_ORIGINAL)
            .error(R.drawable.nofoto)
            .into(holder.photo!!)


    }

    override fun getItemCount(): Int {
        return thing.images.size
    }
}