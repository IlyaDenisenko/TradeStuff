package com.zil.tradestuff.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.model.ThingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class DescriptionThingRecyclerAdapter(val thing: ThingModel, val id: Int):
    RecyclerView.Adapter<DescriptionThingRecyclerAdapter.MyHolder>() {

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var photo: ImageView? = null
        init {
            photo = itemView.findViewById(R.id.description_photo_recycler_view)
        }
    }

    val myCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_description_thing, parent, false)
        return MyHolder(itemView)
    }
// TODO (Проверять наличие фото в телефоне. Да(выгружать с телефона - Нет(выгружать из storage))
    override fun onBindViewHolder(holder: MyHolder, position: Int) {

    myCoroutineScope.launch(Dispatchers.Main) {
        val def = async(Dispatchers.IO){
                MainActivity.SingletonFirebaseStorage.getFirebaseStorage
                    .getReference("photo_thing")
                    .child(thing.userId.toString())
                    .child(thing.name)
                    .child(File(ImagesConverter.fromStringToUri(thing.images)[position].path.toString()).name)
                    .getBytes((1024 * 1024) * 6)
        }
        def.await().addOnSuccessListener{
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            Glide.with(holder.itemView.context)
                .load(bitmap)
                 .override(SIZE_ORIGINAL)
                .error(R.drawable.nofoto)
                .into(holder.photo!!)
        }
    }
        /*val listUri = thing.images
        var uri = listUri[position]
        Glide.with(holder.itemView.context)
            .load(uri)
           // .override(SIZE_ORIGINAL)
            .error(R.drawable.nofoto)
            .into(holder.photo!!)*/
    }

    override fun getItemCount(): Int {
        return thing.images.size
    }
}