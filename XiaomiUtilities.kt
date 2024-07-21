package com.gpsmap.streetview.voicenavigation.livemap.utils

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.text.TextUtils
import android.util.Log
import java.lang.reflect.Method


object XiaomiUtilities {
    // custom permissions
    const val OP_ACCESS_XIAOMI_ACCOUNT = 10015
    const val OP_AUTO_START = 10008

    /**
     * Open new windows while running in the background
     */
    const val OP_BACKGROUND_START_ACTIVITY = 10021
    const val OP_BLUETOOTH_CHANGE = 10002
    const val OP_BOOT_COMPLETED = 10007
    const val OP_DATA_CONNECT_CHANGE = 10003
    const val OP_DELETE_CALL_LOG = 10013
    const val OP_DELETE_CONTACTS = 10012
    const val OP_DELETE_MMS = 10011
    const val OP_DELETE_SMS = 10010
    const val OP_EXACT_ALARM = 10014
    const val OP_GET_INSTALLED_APPS = 10022
    const val OP_GET_TASKS = 10019
    const val OP_INSTALL_SHORTCUT = 10017
    const val OP_NFC = 10016
    const val OP_NFC_CHANGE = 10009
    const val OP_READ_MMS = 10005
    const val OP_READ_NOTIFICATION_SMS = 10018
    const val OP_SEND_MMS = 10004
    const val OP_SERVICE_FOREGROUND = 10023
    const val OP_SHOW_WHEN_LOCKED = 10020
    const val OP_WIFI_CHANGE = 10001
    const val OP_WRITE_MMS = 10006

    fun isMIUI(): Boolean {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))
    }

    @SuppressLint("PrivateApi")
    private fun getSystemProperty(key: String): String? {
        try {
            val props = Class.forName("android.os.SystemProperties")
            return props.getMethod("get", String::class.java).invoke(null, key) as String
        } catch (ignore: Exception) {
        }
        return null
    }

    fun isCustomPermissionGranted(context: Context, permission: Int): Boolean {
        try {
            val mgr = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val m: Method = AppOpsManager::class.java.getMethod(
                "checkOpNoThrow",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                String::class.java
            )
            val result = m.invoke(mgr, permission, Process.myUid(), context.packageName) as Int
            return result == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            Log.e("XiaomiUtils", e.toString())
        }
        return true
    }

    fun getPermissionManagerIntent(context: Context): Intent =
        Intent("miui.intent.action.APP_PERM_EDITOR").apply {
            putExtra("extra_package_uid", Process.myUid())
            putExtra("extra_pkgname", context.packageName)
            putExtra("extra_package_name", context.packageName)
        }
}