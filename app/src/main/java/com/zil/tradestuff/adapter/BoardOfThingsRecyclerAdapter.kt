package com.zil.tradestuff.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

import com.zil.tradestuff.R
import com.zil.tradestuff.model.ThingModel
import java.text.SimpleDateFormat

class BoardOfThingsRecyclerAdapter(val listThings : List<ThingModel>, onThingClickListener: OnThingClickListener):
    RecyclerView.Adapter<BoardOfThingsRecyclerAdapter.MyViewHolder>(){


    interface OnThingClickListener{
        fun onClickItem(thingModel: ThingModel, position: Int)

        fun onClickDeleteItem(position: Int)
    }

    var onThingOnClickListener: OnThingClickListener = onThingClickListener

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var photoImage : ImageView? = null
        var descriptionText : TextView? = null
        var dateText : TextView? = null
        var deleteItemBut: ImageButton? = null

        init {
            photoImage = itemView.findViewById(R.id.image_photo)
            descriptionText = itemView.findViewById(R.id.description_thing)
            dateText = itemView.findViewById(R.id.date_thing)
            deleteItemBut = itemView.findViewById(R.id.delete_item_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board_of_thing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val thingModel = listThings[position]
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val stringDate = dateFormat.format(listThings[position].date)

        holder.descriptionText?.text = listThings[position].name
        holder.dateText?.text = stringDate
        val nameImageString = listThings[position].images[0].toString().removePrefix("[")
        if (listThings[position].images.isNotEmpty())
            Glide.with(holder.itemView.context)
                .load(nameImageString.toUri())
                .centerCrop()
                .override(SIZE_ORIGINAL)
                .error(R.drawable.nofoto)
                .into(holder.photoImage!!)
        else
            Glide.with(holder.itemView.context)
                .load(R.drawable.nofoto)
                .into(holder.photoImage!!)


        holder.itemView.setOnClickListener{
            onThingOnClickListener.onClickItem(thingModel, position)
        }

        holder.deleteItemBut?.setOnClickListener {
            onThingOnClickListener.onClickDeleteItem(position)
        }
    }

    override fun getItemCount(): Int {
        return listThings.size
    }
}



