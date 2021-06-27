package com.nikitakrapo.android.stocks.domain.model

sealed class NetworkResult<out T: Any>{
    data class Success<out T : Any>(
        val data: T
    ) : NetworkResult<T>()

    data class Error(
        val exception: Exception,
        val errorCode: HttpStatusCode = HttpStatusCode.Unknown
    ) : NetworkResult<Nothing>()
}
