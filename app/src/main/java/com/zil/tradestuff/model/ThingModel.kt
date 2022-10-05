package com.zil.tradestuff.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.model.ThingModel.Companion.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
@TypeConverters(ImagesConverter::class)
data class ThingModel(
    @PrimaryKey(autoGenerate = true)
                      @ColumnInfo(name = "thingId")
                      val id: Int = 0,
    @ColumnInfo(name = "thingImages")
                      val images: List<Uri>,
    @ColumnInfo(name = "thingName")
                      val name: String,
    @ColumnInfo(name = "thingDate")
                      val date: Date
){
    companion object{
        const val TABLE_NAME = "things_table"
        const val DATABASE_THINGS_NAME = "database_things"
    }
}
