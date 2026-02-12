package org.example.geoblinker.core.session


import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set


/**
 * Управление сессией пользователя
 * Хранит: token, hash, sid, sidFamily
 */
class SessionManager(private val settings: Settings) {
    
    
    // Main API
    fun saveToken(token: String) = settings.set("token", token)
    fun getToken(): String = settings["token", ""]
    
    
    fun saveHash(hash: String) = settings.set("hash", hash)
    fun getHash(): String = settings["hash", ""]
    
    
    // IMEI API
    fun saveSid(sid: String) = settings.set("sid", sid)
    fun getSid(): String = settings["sid", ""]
    
    
    fun saveSidFamily(sidFamily: String) = settings.set("sidFamily", sidFamily)
    fun getSidFamily(): String = settings["sidFamily", ""]
    
    
    // Units
    fun saveUnitsDistance(isKm: Boolean) = settings.set("unitsDistance", isKm)
    fun getUnitsDistance(): Boolean = settings["unitsDistance", true]
    
    
    fun saveUpdateMap(enabled: Boolean) = settings.set("updateMap", enabled)
    fun getUpdateMap(): Boolean = settings["updateMap", true]
    
    
    // Auth state
    fun isAuthenticated(): Boolean = getToken().isNotEmpty() && getHash().isNotEmpty()
    
    
    fun clearSession() {
        settings.clear()
    }
}
