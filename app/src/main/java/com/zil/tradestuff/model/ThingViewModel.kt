package com.zil.tradestuff.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zil.tradestuff.server.ContractDBInterface
import com.zil.tradestuff.server.InteractionFirebaseFirestore
import com.zil.tradestuff.server.InteractionFirebaseStorage

class ThingViewModel(application: Application) : AndroidViewModel(application) {

    var contractDBInterface = InteractionFirebaseFirestore()
    val connectorFirebaseStorage = InteractionFirebaseStorage()
    private var liveData: MutableLiveData<List<ThingModel>> = MutableLiveData()

   // private val database : AppDatabase = AppDatabase.getInstance(application)


    fun allThingLiveData(callbackServerData: ContractDBInterface.CallbackServerData): LiveData<List<ThingModel>> {
        liveData.value = contractDBInterface.getAllFromDB(callbackServerData)
        Log.i("gruff", "ThingViewModel")
        return liveData
    }


    fun insertThing(thing: ThingModel){
        contractDBInterface.insertInDB(thing)
        connectorFirebaseStorage.insertInDB(thing)
      //  database.getThingDao().insertThing(thing)
    }

    fun getThingsByUser(userId: String, callbackServerData: ContractDBInterface.CallbackServerData): List<ThingModel>{
        return contractDBInterface.getThingsByUserFromDB(userId, callbackServerData)
    }

    fun deleteThing(thing: ThingModel){
        contractDBInterface.deleteFromDB(thing)
        connectorFirebaseStorage.deleteFromDB(thing)
    }
}