package com.zil.tradestuff.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.zil.tradestuff.server.ContractDBInterface
import com.zil.tradestuff.server.InteractionFirebase
import kotlinx.coroutines.CoroutineScope

class ThingViewModel(application: Application) : AndroidViewModel(application) {

    var contractDBInterface = InteractionFirebase()
    private var liveData: MutableLiveData<List<ThingModel>> = MutableLiveData()

   // private val database : AppDatabase = AppDatabase.getInstance(application)


    fun allThingLiveData(callbackServerData: ContractDBInterface.CallbackServerData): LiveData<List<ThingModel>> {
        liveData.value = contractDBInterface.getAllFromDB(callbackServerData)
        Log.i("gruff", "ThingViewModel")
        return liveData
    }


    fun insertThing(thing: ThingModel){
      //  database.getThingDao().insertThing(thing)
    }

    fun selectThing(id: Int): LiveData<ThingModel>{
        return liveData {  }
      //  return database.getThingDao().getThingById(id)
    }

    fun deleteThing(thing: ThingModel, callbackServerData: ContractDBInterface.CallbackServerData){
      contractDBInterface.deleteFromDB(thing, callbackServerData)
    }
}