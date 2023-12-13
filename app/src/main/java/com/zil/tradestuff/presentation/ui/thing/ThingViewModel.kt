package com.zil.tradestuff.presentation.ui.thing

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.model.ThingModel
import com.zil.tradestuff.data.ThingFirestoreDataSource
import com.zil.tradestuff.data.ThingStorageDataSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class ThingViewModel(application: Application) : AndroidViewModel(application) {

    private var connectorFirestore = ThingFirestoreDataSource()
    private val connectorFirebaseStorage = ThingStorageDataSource()

    fun insertThing(thing: ThingModel){
        connectorFirestore.insertInDB(thing)
        connectorFirebaseStorage.insertInDB(thing)
    }

    fun getThingsByUser(userId: String): Flow<State<MutableList<ThingModel>>>{
        return connectorFirestore.getThingsByUserFromDB(userId)
    }

    fun getListPhotosByItem(thing: ThingModel): Flow<State<MutableList<Task<Uri>>>>{
        return connectorFirebaseStorage.getPhotoByStorage(thing)
    }
    fun deleteThing(thing: ThingModel){
        connectorFirestore.deleteFromDB(thing)
        connectorFirebaseStorage.deleteFromDB(thing)
    }
}