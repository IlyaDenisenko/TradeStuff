package com.zil.tradestuff.dao

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zil.tradestuff.model.ThingModel

@Database(entities = [ThingModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getThingDao() : ThingDAO

    companion object{
        private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context) : AppDatabase{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, ThingModel.DATABASE_THINGS_NAME).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}