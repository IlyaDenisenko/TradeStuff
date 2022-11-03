package com.zil.tradestuff.server

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zil.tradestuff.model.ThingModel

interface ContractDBInterface {

    fun insertInDB(thingModel: ThingModel)

    fun insertAllInDB(listThings: List<ThingModel>)

    fun getAllFromDB(callbackServerData: CallbackServerData):List<ThingModel>

    fun deleteFromDB(thingModel: ThingModel, callbackServerData: CallbackServerData)

    fun deleteFromDBById(id: Int)

    interface CallbackServerData{
        fun actionAfterComingData()
    }


}