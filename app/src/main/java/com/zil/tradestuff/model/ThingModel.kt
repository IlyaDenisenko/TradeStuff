package com.zil.tradestuff.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zil.tradestuff.dao.ImagesConverter

import java.util.*

//@Entity(tableName = TABLE_NAME)
//@TypeConverters(ImagesConverter::class)
data class ThingModel(
 //   @PrimaryKey(autoGenerate = true)
   //                   @ColumnInfo(name = "thingId")
    val id: Int,
    //@ColumnInfo(name = "thingImages")
    val userId: String?,
    val images: List<String>,
  //  @ColumnInfo(name = "thingName")
    val name: String,
    val description: String,
   // @ColumnInfo(name = "thingDate")
    val date: Date
){
    companion object{
        const val TABLE_NAME = "things_table"
        const val DATABASE_THINGS_NAME = "database_things"
    }
    constructor() : this(0, "user", listOf(), "Name","Description",  Date())
}
