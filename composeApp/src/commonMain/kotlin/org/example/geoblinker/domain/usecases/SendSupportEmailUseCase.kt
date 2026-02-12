package org.example.geoblinker.domain.usecases


import org.example.geoblinker.domain.models.Code
import org.example.geoblinker.domain.repositories.ApiRepository


/**
 * UseCase для отправки email в техподдержку
 */
class SendSupportEmailUseCase(
    private val apiRepository: ApiRepository
) {
    /**
     * Отправка сообщения в техподдержку
     * @param subject - тема письма
     * @param message - текст сообщения
     * @param email - email отправителя
     * @return Result с кодом ответа
     */
    suspend operator fun invoke(
        subject: String,
        message: String,
        email: String
    ): Result<Code> {
        return try {
            val request = mapOf(
                "subject" to subject,
                "message" to message,
                "email" to email
            )
            val response = apiRepository.sendEmailTechSupport(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
