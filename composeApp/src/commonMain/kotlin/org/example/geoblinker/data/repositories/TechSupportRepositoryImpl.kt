package org.example.geoblinker.data.repositories


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.geoblinker.db.AppDatabase
import com.example.geoblinker.db.ChatTechSupportQueries
import com.example.geoblinker.db.MessageTechSupportQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.geoblinker.domain.models.ChatTechSupport
import org.example.geoblinker.domain.models.MessageTechSupport
import org.example.geoblinker.domain.repositories.TechSupportRepository


class TechSupportRepositoryImpl(
    private val db: AppDatabase,
    private val chatQueries: ChatTechSupportQueries,
    private val messageQueries: MessageTechSupportQueries
) : TechSupportRepository {


    override fun getAllChats(): Flow<List<ChatTechSupport>> {
        return chatQueries.getAllChats()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    override suspend fun insertMessage(message: MessageTechSupport) = withContext(Dispatchers.Default) {
        messageQueries.insertMessageTechSupport(
            chatId = message.chatId,
            content = message.content,
            timeStamp = message.timeStamp,
            isMy = message.isMy,
            typeMessage = message.typeMessage,
            photoUri = message.photoUri
        )
    }


    override fun getMessagesChat(chatId: Long): Flow<List<MessageTechSupport>> {
        return messageQueries.getMessagesByChatId(chatId)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }


    override suspend fun insertRequest(chat: ChatTechSupport, message: MessageTechSupport) = withContext(Dispatchers.Default) {
        db.transaction {
            chatQueries.insertChatTechSupport(
                title = chat.title,
                lastMessageTime = chat.lastMessageTime,
                lastChecked = chat.lastChecked,
                decided = chat.decided
            )


            val newChatId = chatQueries.lastInsertRowId().executeAsOne()


            messageQueries.insertMessageTechSupport(
                chatId = newChatId,
                content = message.content,
                timeStamp = message.timeStamp,
                isMy = message.isMy,
                typeMessage = message.typeMessage,
                photoUri = message.photoUri
            )
        }
    }
