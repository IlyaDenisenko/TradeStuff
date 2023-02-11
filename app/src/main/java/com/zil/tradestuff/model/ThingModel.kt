package com.zil.tradestuff.model

import java.util.*

//@Entity(tableName = TABLE_NAME)
//@TypeConverters(ImagesConverter::class)
data class ThingModel(
 //   @PrimaryKey(autoGenerate = true)
   //                   @ColumnInfo(name = "thingId")
    val id: Int,
    //@ColumnInfo(name = "thingImages")
    val userId: String?,
    val userName: String?,
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
    constructor() : this(0, "user", "userName", listOf(), "Name","Description",  Date())
}
