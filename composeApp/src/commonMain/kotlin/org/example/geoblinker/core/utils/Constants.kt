package org.example.geoblinker.core.utils




/**
 * API константы
 */
object ApiConstants {
    // Main API
    const val BASE_URL = "https://ibronevik.ru/taxi/c/0/api/v1/"
    
    
    // IMEI API
    const val IMEI_BASE_URL = "https://www.gps666.net/mapi"
    const val IMEI_LOGIN = "georule"
    const val IMEI_PASSWORD = "8bbe1a8ed834b27261f2a4dfb1418ae7"
    
    
    // Timeout
    const val TIMEOUT_SECONDS = 30L
    
    
    // Retry
    const val MAX_RETRIES = 3
    const val RETRY_DELAY_MS = 1000L
}


/**
 * Константы приложения
 */
object AppConstants {
    const val APP_NAME = "GeoBlinker"
    const val APP_VERSION = "1.0.0"
    
    
    // Preferences keys
    const val PREF_AUTH_TOKEN = "auth_token"
    const val PREF_AUTH_HASH = "auth_hash"
    const val PREF_USER_PHONE = "user_phone"
    const val PREF_USER_ID = "user_id"
    const val PREF_SESSION_ID = "session_id"
    
    
    // Map
    const val DEFAULT_ZOOM = 15f
    const val MIN_ZOOM = 5f
    const val MAX_ZOOM = 20f
    const val DEFAULT_LATITUDE = 55.751244
    const val DEFAULT_LONGITUDE = 37.618423
    
    
    // Sync
    const val SYNC_INTERVAL_MS = 5000L // 5 секунд
    const val AUTO_REFRESH_ENABLED = true
    
    
    // Database
    const val DATABASE_NAME = "geoblinker.db"
}


/**
 * Константы валидации
 */
object ValidationConstants {
    const val PHONE_LENGTH = 11
    const val CODE_LENGTH = 6
    const val IMEI_LENGTH = 15
    
    
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_PASSWORD_LENGTH = 32
    
    
    const val MIN_NAME_LENGTH = 2
    const val MAX_NAME_LENGTH = 50
    
    
    // Email regex
    const val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
}


/**
 * Константы UI
 */
object UiConstants {
    const val ANIMATION_DURATION_MS = 300
    const val DEBOUNCE_DELAY_MS = 500L
    const val TOAST_DURATION_SHORT = 2000L
    const val TOAST_DURATION_LONG = 3500L
}


/**
 * Константы для карты
 */
object MapConstants {
    const val DEFAULT_MARKER_TYPE = "m_0"
    const val MARKER_CLUSTER_ENABLED = true
    const val MIN_CLUSTER_SIZE = 2
    
    
    // Расстояние в метрах для определения "близких" устройств
    const val NEARBY_DISTANCE_METERS = 100.0
}


/**
 * Константы ошибок
 */
object ErrorConstants {
    const val ERROR_NETWORK = "Ошибка сети. Проверьте подключение к интернету."
    const val ERROR_AUTH = "Ошибка авторизации. Войдите заново."
    const val ERROR_SERVER = "Ошибка сервера. Попробуйте позже."
    const val ERROR_UNKNOWN = "Неизвестная ошибка."
    const val ERROR_VALIDATION = "Проверьте правильность введённых данных."
}
