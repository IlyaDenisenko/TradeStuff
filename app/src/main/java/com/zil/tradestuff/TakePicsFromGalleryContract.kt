package com.zil.tradestuff

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TakePicsFromGalleryContract : ActivityResultContract<Intent, Intent?>() {
    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return intent
    }
}