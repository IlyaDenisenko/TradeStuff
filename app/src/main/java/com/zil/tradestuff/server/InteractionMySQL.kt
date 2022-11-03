package com.zil.tradestuff.server

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zil.tradestuff.dao.AppDatabase
import com.zil.tradestuff.model.ThingModel

class InteractionMySQL(context: Context): ContractDBInterface {

    private val database: AppDatabase = AppDatabase.getInstance(context)


    override fun insertInDB(thingModel: ThingModel) {
        database.getThingDao().insertThing(thingModel)
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }

    override fun getAllFromDB(callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel> {
       return database.getThingDao().getAllThings()

    }

    override fun deleteFromDB(thingModel: ThingModel, callbackServerData: ContractDBInterface.CallbackServerData) {
        TODO("Not yet implemented")
    }

    override fun deleteFromDBById(id: Int) {
        database.getThingDao().deleteThingById(id)
    }
}