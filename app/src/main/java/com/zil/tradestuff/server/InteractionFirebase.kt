package com.zil.tradestuff.server

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zil.tradestuff.model.ThingModel

class InteractionFirebase: ContractDBInterface {

    val docRef = FirebaseFirestore.getInstance()
        .collection("Publication").document(FirebaseAuth.getInstance().uid!!).collection("Things").document()

    override fun sendInDB(thingModel: ThingModel) {
        docRef.set(thingModel)
    }

    override fun getAllFromDB(): List<ThingModel> {
        var thingModel: ThingModel

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    thingModel = document.toObject(ThingModel::class.java)!!
                }
            }
        return thingModel
    }

    override fun deleteFromDB(thingModel: ThingModel) {
        TODO("Not yet implemented")
    }
}