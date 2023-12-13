package com.zil.tradestuff.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zil.tradestuff.domain.model.ThingModel

@Dao
interface ThingDAO {

    companion object{
        const val TABLE_NAME = "things_table"
        const val DATABASE_THINGS_NAME = "database_things"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThing(thing: ThingModel)

    @Update
    fun updateThing(thing: ThingModel)

    @Delete
    fun deleteThing(thing: ThingModel)

    @Query("DELETE FROM $TABLE_NAME WHERE thingId == :id")
    fun deleteThingById(id: Int)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllThings() : List<ThingModel>

    @Query("SELECT * FROM $TABLE_NAME WHERE thingId == :id")
    fun getThingById(id : Int) : LiveData<ThingModel>
}
