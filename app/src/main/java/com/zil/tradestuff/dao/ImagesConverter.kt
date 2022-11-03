package com.zil.tradestuff.dao

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class ImagesConverter {
    companion object{

        fun fromUriToString(imagesUri: List<Uri>): List<String>{
            var imagesStr : MutableList<String> = mutableListOf()
            for (n in imagesUri.indices){
                Log.i("size images: ", imagesStr.size.toString())
                imagesStr.add(n, imagesUri[n].toString())
            }
            return imagesStr
        }

        fun fromStringToUri(imagesString: List<String>): List<Uri>{
            val listImageUri: MutableList<Uri> = mutableListOf()
            for (n in imagesString.indices)
                listImageUri.add(n, imagesString[n].toUri())
            return listImageUri
        }

        @TypeConverter
        @JvmStatic
        fun fromImages(images : List<Uri> ) : String {
          /*  var image = images.toString()
            image.removePrefix("[").removeSuffix("]")*/
            val imagesStr : MutableList<String> = mutableListOf()
            var image : String = ""
            for (n in images.indices){
                Log.i("size images: ", imagesStr.size.toString())
                imagesStr.add(n, images[n].toString())
            }
            image = imagesStr.stream().collect(Collectors.joining(","))
            return image
        }

        @TypeConverter
        @JvmStatic
        fun toImages(data : String) : List<Uri> {
            val listUri : MutableList<Uri> = mutableListOf()
            val listStr : List<String> = data.split(",")
            for (n in listStr.indices)
                listUri.add(n, listStr[n].toUri())
            return listUri
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date : Date) : Long{
            return date.time
        }

        @TypeConverter
        @JvmStatic
        fun toDate(value : Long): Date{
            return Date(value)
        }
    }


}