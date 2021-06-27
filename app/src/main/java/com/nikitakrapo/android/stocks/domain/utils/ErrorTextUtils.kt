package com.nikitakrapo.android.stocks.domain.utils

import android.content.Context
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.domain.model.HttpStatusCode
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import java.net.UnknownHostException

class ErrorTextUtils {
    companion object{
        private const val TAG = "ErrorTextUtils"

        fun getErrorSnackBarText(networkResultError: NetworkResult.Error, context: Context): String{
            if (networkResultError.errorCode != HttpStatusCode.Unknown)
                return getHttpStatusCodeFormatted(networkResultError.errorCode, context)

            return when(networkResultError.exception.cause){
                is UnknownHostException -> context.getString(R.string.unknown_host_exception)

                else -> {
                    networkResultError.exception.localizedMessage
                        ?: context.getString(R.string.unknown_exception)
                }
            }
        }

        private fun getHttpStatusCodeFormatted(httpStatusCode: HttpStatusCode, context: Context): String{
            return when(httpStatusCode){
                HttpStatusCode.TooManyRequests -> context.getString(R.string.too_many_requests)
                else -> httpStatusCode.name
            }
        }
    }
}