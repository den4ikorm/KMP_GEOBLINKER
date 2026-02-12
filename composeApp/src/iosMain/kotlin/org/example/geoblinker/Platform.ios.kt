package org.example.geoblinker.core.platform

import platform.UIKit.UIDevice

/**
 * iOS platform implementation
 */
actual object Platform {
    actual val name: String = "iOS"
    
    actual val version: String = UIDevice.currentDevice.systemVersion
    
    actual val deviceModel: String = UIDevice.currentDevice.model
}

/**
 * Returns iOS platform type
 */
actual fun getPlatformType(): PlatformType = PlatformType.IOS
