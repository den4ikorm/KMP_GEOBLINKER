package org.example.geoblinker.core




/**
 * Sealed class для результатов сетевых запросов
 */
sealed class NetworkResult<out T> {
    /**
     * Успешный результат
     */
    data class Success<T>(val data: T) : NetworkResult<T>()
    
    
    /**
     * Ошибка
     */
    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : NetworkResult<Nothing>()
    
    
    /**
     * Загрузка
     */
    data object Loading : NetworkResult<Nothing>()
}


/**
 * Extension функции для удобства
 */
fun <T> NetworkResult<T>.isSuccess(): Boolean = this is NetworkResult.Success
fun <T> NetworkResult<T>.isError(): Boolean = this is NetworkResult.Error
fun <T> NetworkResult<T>.isLoading(): Boolean = this is NetworkResult.Loading


fun <T> NetworkResult<T>.getOrNull(): T? = when (this) {
    is NetworkResult.Success -> data
    else -> null
}


fun <T> NetworkResult<T>.getErrorMessage(): String? = when (this) {
    is NetworkResult.Error -> message
    else -> null
}
