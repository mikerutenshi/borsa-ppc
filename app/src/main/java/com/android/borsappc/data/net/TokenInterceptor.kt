package com.android.borsappc.data.net

import androidx.datastore.core.DataStore
import com.android.borsappc.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor(private val userPreferences: DataStore<UserPreferences>) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val accessToken = runBlocking {
            userPreferences.data.first().signInPrefs.accessToken
        }
        val request: Request = if (chain.request().url.encodedPath.contains("/users")
            && chain.request().method == "POST"
        ) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer $accessToken"
                )
                .url(chain.request().url)
                .build()
        }
        return chain.proceed(request)
    }
}