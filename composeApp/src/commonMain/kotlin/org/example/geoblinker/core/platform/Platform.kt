package org.example.geoblinker.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
