package org.example.geoblinker.domain.repositories


import app.cash.sqldelight.db.QueryResult
import kotlinx.coroutines.flow.Flow
import org.example.geoblinker.domain.models.*


interface TechSupportRepository {


    fun getAllChats(): Flow<List<ChatTechSupport>>


    suspend fun insertMessage(message: MessageTechSupport): QueryResult<Long>


    fun getMessagesChat(chatId: Long): Flow<List<MessageTechSupport>>


    suspend fun insertRequest(chat: ChatTechSupport, message: MessageTechSupport)


