package com.faltenreich.diaguard.navigation.system

import android.content.Context
import android.content.Intent
import android.provider.Settings

class AndroidSystemSettings(private val context: Context) : SystemSettings {

    override fun openNotificationSettings() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }
}