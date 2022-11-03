package com.zil.tradestuff.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.server.ContractDBInterface
import com.zil.tradestuff.server.InteractionFirebase

class DashboardViewModel : ViewModel() {

    var contractDBInterface = InteractionFirebase()
    private var liveData: MutableLiveData<List<ThingModel>> = MutableLiveData()

    fun allThingLiveData(callbackServerData: ContractDBInterface.CallbackServerData): LiveData<List<ThingModel>> {
        liveData.value = contractDBInterface.getAllFromDB(callbackServerData)
        Log.i("gruff", "DashboardViewModel")
        return liveData
    }
}