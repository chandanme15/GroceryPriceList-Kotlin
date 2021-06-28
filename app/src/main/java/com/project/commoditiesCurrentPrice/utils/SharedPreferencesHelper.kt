package com.project.commoditiesCurrentPrice.utils

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.project.commoditiesCurrentPrice.MyApplication

object SharedPreferencesHelper {
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(MyApplication.instance.packageName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), MyApplication.instance,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    var dBEntryTime: String?
        get() = sharedPreferences.getString(Constants.DatabaseTime, null)
        set(date) {
            sharedPreferences.edit().putString(Constants.DatabaseTime, date).apply()
        }
}