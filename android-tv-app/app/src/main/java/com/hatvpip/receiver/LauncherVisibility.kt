package com.hatvpip.receiver

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

object LauncherVisibility {
    private const val LAUNCHER_ALIAS_CLASS = "com.hatvpip.receiver.LauncherActivity"

    fun isVisible(context: Context): Boolean {
        val component = launcherComponent(context)
        return context.packageManager.getComponentEnabledSetting(component) !=
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }

    fun setVisible(context: Context, visible: Boolean) {
        val state = if (visible) {
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } else {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        }
        context.packageManager.setComponentEnabledSetting(
            launcherComponent(context),
            state,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun launcherComponent(context: Context): ComponentName =
        ComponentName(context.packageName, LAUNCHER_ALIAS_CLASS)
}
