package com.android.borsappc.data.net

import androidx.datastore.core.DataStore
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.model.Username
import com.android.borsappc.data.net.response.GenericResponse
import com.android.borsappc.data.net.service.AuthService
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import dagger.Lazy
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException
import java.util.*

class ErrorInterceptor(
    private val userPreferences: DataStore<UserPreferences>,
    private val retrofit: Lazy<Retrofit>
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val response: Response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            if (response.code == 500) {
                throw IOException("Terjadi kesalahan pada server")
            } else if (response.code == 404 && response.message.lowercase(Locale.getDefault())
                    .contains("not found")
            ) {
                throw IOException("Server endpoint tidak ditemukan")
            } else if (response.body != null) {
                val jsonParser = JsonParser()
                try {
                    val jsonElement: JsonElement = jsonParser.parse(response.body!!.string())
                    if (jsonElement.isJsonObject) {
                        try {
                            val jsonObject: JsonObject = jsonElement.asJsonObject
                            if (jsonObject.has("message")) {
                                if (jsonObject.get("message").getAsString()
                                        .lowercase(Locale.getDefault())
                                        .contains("refresh token is expired")
                                ) {
                                    signOut()
                                }
                                throw IOException(jsonObject.get("message").getAsString())
                            }
                        } catch (ex: JsonSyntaxException) {
                            Timber.d("Json syntax exception when getting object")
                            throw IOException(response.code.toString() + " | " + response.message)
                        }
                    } else {
                        Timber.d("Not a json object")
                        throw IOException(response.code.toString() + " | " + response.message)
                    }
                } catch (ex: JsonSyntaxException) {
                    Timber.d("Json syntax exception when parsing")
                    throw IOException(response.code.toString() + " | " + response.message)
                }
            }
        }
        return response
    }

    @Throws(IOException::class)
    private fun signOut() {
        synchronized(this) {
            runBlocking {
                val username = userPreferences.data.last().signInPrefs.username
                val userNameModel = Username(username)
                val signOutResponse: GenericResponse<Unit>? =
                    retrofit.get().create(AuthService::class.java).signOut(userNameModel)

                if (signOutResponse != null &&
                    signOutResponse.status.lowercase(Locale.ROOT) == "ok") {
                    // TODO navigate to auth screen
                    userPreferences.updateData { it.toBuilder().clear().build() }
                } else {
                    throw IOException("My sign out failed")
                }
            }
        }
    }
}