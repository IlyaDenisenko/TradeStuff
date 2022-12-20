package com.zil.tradestuff.server

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zil.tradestuff.model.ThingModel

class InteractionFirebaseFirestore: ContractDBInterface {

    var listAllThings: MutableList<ThingModel> = mutableListOf()
    var listUserThings: MutableList<ThingModel> = mutableListOf()
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()

    val docRefAll = firebaseFirestore
        .collectionGroup("Things")
    /**Ссылка для добавления товара в БД*/
    val docRef = FirebaseFirestore.getInstance()
        .collection("Publication")
        .document(firebaseAuth.uid!!)
        .collection("Things")


    override fun insertInDB(thingModel: ThingModel) {
        docRef.document(thingModel.name).set(thingModel)
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }

    /**Get all documents all users */
    override fun getAllFromDB(callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel> {
            docRefAll.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        snapshot.documents.forEach {
                            val thingModel = it.toObject(ThingModel::class.java)!!
                            if (!listAllThings.contains(thingModel)){
                                listAllThings.add(thingModel)
                            }
                        }
                        Log.i("gruff", "Firebase: " + listAllThings.toString())
                        callbackServerData.actionAfterComingData()
                    }
                }
        return listAllThings
    }
/**Get all documents current user */
    override fun getThingsByUserFromDB(userId: String, callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel> {
       firebaseFirestore
           .collection("Publication")
           .document(userId)
           .collection("Things")
           .get()
           .addOnSuccessListener { snap -> snap.documents.forEach {
               val thing = it.toObject(ThingModel::class.java)!!
               listUserThings.add(thing)
               }
               Log.i("gruff", "Firebase user: " + listUserThings.toString())
               callbackServerData.actionAfterComingData()
           }
        return listUserThings
    }

    override fun deleteFromDB(thingModel: ThingModel, callbackServerData: ContractDBInterface.CallbackServerData) {
        docRef.document(thingModel.name).delete()
        callbackServerData.actionAfterComingData()
    }

    override fun deleteByIdFromDB(id: Int) {
        docRef.document().delete()
    }
}