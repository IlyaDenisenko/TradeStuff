package com.zil.tradestuff

import android.app.Activity
import androidx.appcompat.app.AlertDialog

class LoadingDialog(myActivity: Activity) {

    lateinit var alertDialog: AlertDialog
    var activity: Activity = myActivity

    fun startDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading, null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismissDialog(){
        alertDialog.dismiss()
    }
}