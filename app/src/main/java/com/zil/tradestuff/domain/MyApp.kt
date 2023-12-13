package com.zil.tradestuff.domain

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MyApp(): Application() {


    object firebaseAuth{
        fun getFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }
    }

    object firebaseFirestore {
        fun getFirebaseFirestore(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
        }
    }

        object firebaseStorage {
            fun getFirebaseStorage(): FirebaseStorage {
                return FirebaseStorage.getInstance()
            }
        }
    }