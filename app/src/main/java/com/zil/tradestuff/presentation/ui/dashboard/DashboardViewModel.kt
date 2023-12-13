package com.zil.tradestuff.presentation.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.model.ThingModel
import com.zil.tradestuff.data.ThingFirestoreDataSource
import kotlinx.coroutines.flow.Flow

class DashboardViewModel : ViewModel() {

    private var connectorFirestore = ThingFirestoreDataSource()
    private var liveData: MutableLiveData<List<ThingModel>> = MutableLiveData()

    fun getAllData(): Flow<State<MutableList<ThingModel>>> {
        return connectorFirestore.getAllThingFromDB()
    }
}