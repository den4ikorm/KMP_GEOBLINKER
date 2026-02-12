package org.example.geoblinker.core.platform

import android.os.Build

/**
 * Android platform implementation
 */
actual object Platform {
    actual val name: String = "Android"
    
    actual val version: String = "${Build.VERSION.SDK_INT}"
    
    actual val deviceModel: String = "${Build.MANUFACTURER} ${Build.MODEL}"
}

/**
 * Returns Android platform type
 */
actual fun getPlatformType(): PlatformType = PlatformType.ANDROID
