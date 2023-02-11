package com.zil.tradestuff.adapter


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.zil.tradestuff.MainActivity

import com.zil.tradestuff.R
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.model.ThingModel
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat

class BoardOfThingsRecyclerAdapter(val listThings : List<ThingModel>, onThingClickListener: OnThingClickListener):
    RecyclerView.Adapter<BoardOfThingsRecyclerAdapter.MyViewHolder>(){


    interface OnThingClickListener{
        fun onClickItem(thingModel: ThingModel, position: Int)
    }

    var onThingOnClickListener: OnThingClickListener = onThingClickListener
    val SIX_MEGABYTES: Long = (1024 * 1024) * 6
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
// TODO (Проверять наличие фото в телефоне. Да(выгружать с телефона - Нет(выгружать из storage))
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val thingModel = listThings[position]
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val stringDate = dateFormat.format(listThings[position].date)
        var bitmap: Bitmap

        myCoroutineScope.launch(Dispatchers.Main) {

            val def = async(Dispatchers.IO) {
                MainActivity.SingletonFirebaseStorage.getFirebaseStorage
                    .getReference("photo_thing")
                    .child(thingModel.userId.toString())
                    .child(thingModel.name)
                    .child(File(ImagesConverter.fromStringToUri(thingModel.images)[0].path.toString()).name)
                    .getBytes(SIX_MEGABYTES)

            }
            def.await().addOnSuccessListener {
                bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                if (listThings[position].images.isNotEmpty())
                    Glide.with(holder.itemView.context)
                        .load(bitmap)
                        .centerCrop()
                        .override(SIZE_ORIGINAL)
                        .error(R.drawable.nofoto)
                        .into(holder.photoImage!!)
                else
                    Glide.with(holder.itemView.context)
                        .load(R.drawable.nofoto)
                        .into(holder.photoImage!!)
            }
            }



        holder.nameText?.text = listThings[position].name
        holder.dateText?.text = stringDate
        val nameImageString = listThings[position].images[0].removePrefix("[")



        holder.itemView.setOnClickListener{
            onThingOnClickListener.onClickItem(thingModel, position)
        }


    }

    override fun getItemCount(): Int {
        return listThings.size
    }
}



