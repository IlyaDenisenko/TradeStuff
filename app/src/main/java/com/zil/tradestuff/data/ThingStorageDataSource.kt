package com.zil.tradestuff.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.domain.model.ThingModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class ThingStorageDataSource: ContractDBInterface {

    val storageReference = MyApp.firebaseStorage.getFirebaseStorage().getReference("photo_thing").child(MyApp.firebaseAuth.getFirebaseAuth().uid.toString())

    override fun insertInDB(thingModel: ThingModel) {
        for (n in 0 until thingModel.images.size) {
            storageReference.child(thingModel.name)
                .child(File(ImagesConverter.fromStringToUri(thingModel.images)[n].path.toString()).name)
                .putFile(ImagesConverter.fromStringToUri(thingModel.images)[n])
        }
    }

    override fun insertAllInDB(listThings: List<ThingModel>) {
        TODO("Not yet implemented")
    }

    override fun getAllThingFromDB(): Flow<State<MutableList<ThingModel>>> {
        TODO("Not yet implemented")
    }

    override fun getThingsByUserFromDB(userId: String): Flow<State<MutableList<ThingModel>>> {
        TODO("Not yet implemented")
    }

    fun getPhotoByStorage(thing: ThingModel): Flow<State<MutableList<Task<Uri>>>> {
        val listPhoto: MutableList<Task<Uri>> = mutableListOf()
        val data: Flow<State<MutableList<Task<Uri>>>> = flow{
            emit(State.loading())

            MyApp.firebaseStorage.getFirebaseStorage()
                .getReference("photo_thing")
                .child(thing.userId.toString())
                .child(thing.name)
                .listAll().addOnSuccessListener {
                    for (item in it.items)
                        listPhoto.add(item.downloadUrl)
                }.await()
            emit(State.success(listPhoto))
        }
        return data
    }

    override fun deleteFromDB(thingModel: ThingModel) {
        for (n in 0 until thingModel.images.size) {
            storageReference.child(thingModel.name)
                .child(File(ImagesConverter.fromStringToUri(thingModel.images)[n].path.toString()).name)
                .delete()
        }
    }

    override fun deleteByIdFromDB(id: Int) {
        TODO("Not yet implemented")
    }
}