package com.zil.tradestuff.server

import com.zil.tradestuff.model.ThingModel

interface ContractDBInterface {

    fun insertInDB(thingModel: ThingModel)

    fun insertAllInDB(listThings: List<ThingModel>)

    fun getAllFromDB(callbackServerData: CallbackServerData):List<ThingModel>

    fun getThingsByUserFromDB(userId: String, callbackServerData: CallbackServerData): List<ThingModel>

    fun deleteFromDB(thingModel: ThingModel)

    fun deleteByIdFromDB(id: Int)

    interface CallbackServerData{
        fun actionAfterComingData()
    }


}