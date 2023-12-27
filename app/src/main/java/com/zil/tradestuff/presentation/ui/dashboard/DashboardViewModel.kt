package com.zil.tradestuff.presentation.ui.dashboard

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.model.ThingModel
import com.zil.tradestuff.data.ThingFirestoreDataSource
import com.zil.tradestuff.data.ThingStorageDataSource
import kotlinx.coroutines.flow.Flow

class DashboardViewModel : ViewModel() {

    private var connectorFirestore = ThingFirestoreDataSource()
    private var connectorFirebaseStorage = ThingStorageDataSource()
    private var liveData: MutableLiveData<List<ThingModel>> = MutableLiveData()

    fun getAllData(): Flow<State<MutableList<ThingModel>>> {
        return connectorFirestore.getAllThingFromDB()
    }

    fun getListPhotosByItem(thing: ThingModel): Flow<State<MutableList<Task<Uri>>>>{
        return connectorFirebaseStorage.getPhotoByStorage(thing)
    }
}