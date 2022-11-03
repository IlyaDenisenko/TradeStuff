package com.zil.tradestuff.server

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.zil.tradestuff.LoadingDialog
import com.zil.tradestuff.model.ThingModel
import kotlin.math.acos

class InteractionFirebase: ContractDBInterface {

    var listThing: MutableList<ThingModel> = mutableListOf()

    val docRefAll = FirebaseFirestore.getInstance()
        .collectionGroup("Things")
    val docRef = FirebaseAuth.getInstance().uid?.let {
        FirebaseFirestore.getInstance()
        .collection("Publication").document(it).collection("Things")
    }

    override fun insertInDB(thingModel: ThingModel) {
        docRef?.document(thingModel.name)?.set(thingModel)
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }



    override fun getAllFromDB(callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel> {
        val callbackServerData = callbackServerData
        var thingModel: ThingModel
            docRefAll.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        snapshot.documents.forEach {
                            thingModel = it.toObject(ThingModel::class.java)!!
                            if (!listThing.contains(thingModel))
                            listThing.add(thingModel)

                            Log.i("gruff", "Firebase: " + listThing.toString())
                            callbackServerData.actionAfterComingData()
                        }
                    }
                }
        return listThing
    }

    fun getAllFromDB2(callbackServerData: ContractDBInterface.CallbackServerData) :List<ThingModel>{
        val callbackServerData = callbackServerData
        var thingModel: ThingModel
        docRef!!.addSnapshotListener { value, error ->
            if (value != null)
                value.documents.forEach {
                    thingModel = it.toObject(ThingModel::class.java)!!
                    if (!listThing.contains(thingModel))
                        listThing.add(thingModel)

                    Log.i("gruff", "Firebase2: " + listThing.toString())
                    callbackServerData.actionAfterComingData()
                }
        }
        return listThing
    }

    override fun deleteFromDB(thingModel: ThingModel, callbackServerData: ContractDBInterface.CallbackServerData) {
        docRef?.document(thingModel.name)?.delete()
        callbackServerData.actionAfterComingData()
    }

    override fun deleteFromDBById(id: Int) {
        docRef?.document()?.delete()
    }
}