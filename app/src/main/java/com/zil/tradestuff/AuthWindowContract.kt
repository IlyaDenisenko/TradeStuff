package com.zil.tradestuff

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class AuthWindowContract: ActivityResultContract<Intent?, Int>() {

    override fun createIntent(context: Context, input: Intent?): Intent {
        return input!!
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int {
        return if (resultCode == RESULT_OK)
            1
        else 0
    }
}