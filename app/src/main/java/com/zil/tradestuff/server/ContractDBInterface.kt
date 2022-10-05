package com.zil.tradestuff.server

import com.zil.tradestuff.model.ThingModel

interface ContractDBInterface {

    fun sendInDB(thingModel: ThingModel)

    fun getAllFromDB(): List<ThingModel>

    fun deleteFromDB(thingModel: ThingModel)
}