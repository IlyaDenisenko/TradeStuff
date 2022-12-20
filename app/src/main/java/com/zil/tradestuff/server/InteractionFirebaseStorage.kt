package com.zil.tradestuff.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zil.tradestuff.model.ThingModel

class InteractionFirebaseStorage: ContractDBInterface {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()
    val storageReference = firebaseStorage.getReference("photo_thing").child(firebaseAuth.uid!!)

    override fun insertInDB(thingModel: ThingModel) {
        TODO("Not yet implemented")
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }

    override fun getAllFromDB(callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel> {
        TODO("Not yet implemented")
    }

    override fun getThingsByUserFromDB(
        userId: String,
        callbackServerData: ContractDBInterface.CallbackServerData
    ): List<ThingModel> {
        TODO("Not yet implemented")
    }

    override fun deleteFromDB(
        thingModel: ThingModel,
        callbackServerData: ContractDBInterface.CallbackServerData
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteByIdFromDB(id: Int) {
        TODO("Not yet implemented")
    }
}