package com.zil.tradestuff.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.domain.model.ThingModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ThingFirestoreDataSource: ContractDBInterface {

    var thing: ThingModel = ThingModel()
    var listAllThings: MutableList<ThingModel> = mutableListOf()
    var listUserThings: MutableList<ThingModel> = mutableListOf()
    val firebaseFirestore = FirebaseFirestore.getInstance()

    val docRefAll = firebaseFirestore
        .collectionGroup("Things")
    /**Ссылка для добавления товара в БД*/
    val docRef = firebaseFirestore
        .collection("Publication")
        .document(MyApp.firebaseAuth.getFirebaseAuth().uid.toString())
        .collection("Things")


    override fun insertInDB(thingModel: ThingModel) {
        docRef.document(thingModel.name).set(thingModel)
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }

    /**Get all the documents of all users */
    override fun getAllThingFromDB(): Flow<State<MutableList<ThingModel>>> {

        val data: Flow<State<MutableList<ThingModel>>> = flow {
            emit(State.loading())

            docRefAll
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        snapshot.documents.forEach {
                            thing = it.toObject(ThingModel::class.java)!!
                            if (!listAllThings.contains(thing)){
                                listAllThings.add(thing)
                            }
                        }
                    }
                }.await()
            emit(State.success(listAllThings))
        }.flowOn(Dispatchers.IO)
        return data
    }


/**Get all the documents of current user */
    override fun getThingsByUserFromDB(userId: String): Flow<State<MutableList<ThingModel>>> {

        val data: Flow<State<MutableList<ThingModel>>> = flow {
        emit(State.loading())

        firebaseFirestore
            .collection("Publication")
            .document(userId)
            .collection("Things")
            .get()
            .addOnSuccessListener { snap ->
                snap.documents.forEach {
                    val thing = it.toObject(ThingModel::class.java)!!
                    listUserThings.add(thing)
                }
            }.await()
        emit(State.success(listUserThings))
    }.flowOn(Dispatchers.IO)
        return data
    }

    override fun deleteFromDB(thingModel: ThingModel) {
        docRef.document(thingModel.name).delete()
    }

    override fun deleteByIdFromDB(id: Int) {
        docRef.document().delete()
    }
}