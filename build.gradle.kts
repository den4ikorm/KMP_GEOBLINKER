plugins {
    // Тот самый плагин, который требует система
    kotlin("multiplatform") version "2.0.20" apply false
    kotlin("plugin.compose") version "2.0.20" apply false
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.compose") version "1.6.11" apply false
    id("app.cash.sqldelight") version "2.0.1" apply false
}
