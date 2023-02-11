package com.zil.tradestuff.common

import android.app.Application
import com.google.firebase.auth.FirebaseAuth

class MyApp: Application() {


    companion object{
        fun getFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }
    }
}