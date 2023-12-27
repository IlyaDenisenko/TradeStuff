package com.zil.tradestuff.domain.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.google.android.gms.tasks.Task
import com.zil.tradestuff.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DescriptionThingRecyclerAdapter(var thing: List<Task<Uri>>):
    RecyclerView.Adapter<DescriptionThingRecyclerAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var photo: ImageView? = null
        init {
            photo = itemView.findViewById(R.id.photo_thing_fragment)
        }
    }

    private val myCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_thing_fragment, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        myCoroutineScope.launch {
            Glide.with(holder.itemView.context)
                .load(thing.get(position).await())
                .override(SIZE_ORIGINAL)
                .error(R.drawable.nofoto)
                .into(holder.photo!!)
        }
    }

    override fun getItemCount(): Int {
        return thing.size
    }
}