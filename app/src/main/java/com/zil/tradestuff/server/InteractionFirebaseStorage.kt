package com.zil.tradestuff.server

import com.google.firebase.storage.FirebaseStorage
import com.zil.tradestuff.common.MyApp
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.model.ThingModel
import java.io.File

class InteractionFirebaseStorage: ContractDBInterface {

    val firebaseStorage = FirebaseStorage.getInstance()
    val storageReference = firebaseStorage.getReference("photo_thing").child(MyApp.getFirebaseAuth().uid!!)

    override fun insertInDB(thingModel: ThingModel) {
        for (n in 0 until thingModel.images.size) {
            storageReference.child(thingModel.name)
                .child(File(ImagesConverter.fromStringToUri(thingModel.images)[n].path.toString()).name)
                .putFile(ImagesConverter.fromStringToUri(thingModel.images)[n])
        }
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

    override fun deleteFromDB(thingModel: ThingModel) {
        for (n in 0 until thingModel.images.size) {
            storageReference.child(thingModel.name)
                .child(File(ImagesConverter.fromStringToUri(thingModel.images)[n].path.toString()).name)
                .delete()
        }
    }

    override fun deleteByIdFromDB(id: Int) {
        TODO("Not yet implemented")
    }
}