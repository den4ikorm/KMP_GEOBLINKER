package org.example.geoblinker.domain.repositories




interface FileService {
    suspend fun saveAvatar(imageBytes: ByteArray): String
    suspend fun deleteAvatar()
    suspend fun getSavedAvatarPath(): String?
    suspend fun saveMedia(bytes: ByteArray, extension: String, type: MediaType): String


    suspend fun saveJournalCsv(content: String, fileName: String)
}
enum class MediaType {
    IMAGE, VIDEO, DOCUMENT
}
