package com.zil.tradestuff.domain.adapter


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.zil.tradestuff.R
import com.zil.tradestuff.domain.model.ThingModel
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

class BoardOfThingsRecyclerAdapter(var listThings : MutableList<ThingModel>, var listPhotoUri: MutableList<Task<Uri>>, onThingClickListener: OnThingClickListener):
    RecyclerView.Adapter<BoardOfThingsRecyclerAdapter.MyViewHolder>(){


    interface OnThingClickListener{
        fun onClickItem(thingModel: ThingModel, position: Int)
    }

    var onThingOnClickListener: OnThingClickListener = onThingClickListener
    val myCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var photoImage : ImageView? = null
        var nameText : TextView? = null
        var dateText : TextView? = null

        init {
            photoImage = itemView.findViewById(R.id.image_photo)
            nameText = itemView.findViewById(R.id.description_thing)
            dateText = itemView.findViewById(R.id.date_thing)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_board_of_thing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val photoUri = listPhotoUri[position]
        val thingModel = listThings[position]
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val stringDate = dateFormat.format(listThings[position].date)

    if (thingModel.images.isNotEmpty())
        myCoroutineScope.launch {
            Glide.with(holder.itemView.context)
                .load(photoUri.await())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .error(R.drawable.nofoto)
                .into(holder.photoImage!!)
        }


        holder.nameText?.text = listThings[position].name
        holder.dateText?.text = stringDate

        holder.itemView.setOnClickListener{
            onThingOnClickListener.onClickItem(thingModel, position)
        }
    }

    fun update(list: MutableList<ThingModel>){
        if(listThings.size > 0){
            listThings.clear()
            listThings.addAll(list)
            notifyDataSetChanged()
        } else{
            listThings = list
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return listThings.size
    }
}



