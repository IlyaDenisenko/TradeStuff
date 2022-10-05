package com.zil.tradestuff.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zil.tradestuff.model.ThingModel

@Dao
interface ThingDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThing(thing: ThingModel)

    @Update
    fun updateThing(thing: ThingModel)

    @Delete
    fun deleteThing(thing: ThingModel)

    @Query("DELETE FROM ${ThingModel.TABLE_NAME} WHERE thingId == :id")
    fun deleteThingById(id: Int)

    @Query("SELECT * FROM ${ThingModel.TABLE_NAME}")
    fun getAllThings() : LiveData<List<ThingModel>>

    @Query("SELECT * FROM ${ThingModel.TABLE_NAME} WHERE thingId == :id")
    fun getThingById(id : Int) : LiveData<ThingModel>
}
