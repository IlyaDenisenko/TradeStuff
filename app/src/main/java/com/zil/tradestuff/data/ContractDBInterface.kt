package com.zil.tradestuff.data

import com.zil.tradestuff.State
import com.zil.tradestuff.domain.model.ThingModel
import kotlinx.coroutines.flow.Flow


interface ContractDBInterface {

    fun insertInDB(thingModel: ThingModel)

    fun insertAllInDB(listThings: List<ThingModel>)

    fun getAllThingFromDB(): Flow<State<MutableList<ThingModel>>>

    fun getThingsByUserFromDB(userId: String): Flow<State<MutableList<ThingModel>>>

    fun deleteFromDB(thingModel: ThingModel)

    fun deleteByIdFromDB(id: Int)


}