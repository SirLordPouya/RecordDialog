package com.pouya.voicerecorder.dialog

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object Util {

    fun formatSeconds(seconds: Int): String {
        return (getTwoDecimalsValue(seconds / 3600) + ":"
                + getTwoDecimalsValue(seconds / 60) + ":"
                + getTwoDecimalsValue(seconds % 60))
    }

    private fun getTwoDecimalsValue(value: Int): String {
        return if (value in 0..9) {
            "0$value"
        } else {
            value.toString() + ""
        }
    }

     fun checkPermission(context: Context): Boolean {
        val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requiredPermissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true

    }
}
