package com.zil.tradestuff.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.zil.tradestuff.dao.AppDatabase

class ThingViewModel(application: Application) : AndroidViewModel(application) {

    private val database : AppDatabase = AppDatabase.getInstance(application)


    internal val allThingLiveData: LiveData<List<ThingModel>> = database.getThingDao().getAllThings()

    fun insertThing(thing: ThingModel){
        database.getThingDao().insertThing(thing)
    }

    fun selectThing(id: Int): LiveData<ThingModel>{
        return database.getThingDao().getThingById(id)
    }

    fun deleteThing(id: Int){
        database.getThingDao().deleteThingById(id)
    }
}