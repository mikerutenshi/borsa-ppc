package com.android.borsappc.data.net

import androidx.datastore.core.DataStore
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.model.UserAccessToken
import com.android.borsappc.data.model.UserRefreshToken
import com.android.borsappc.data.net.datasource.AuthRemoteDataSource
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import dagger.Lazy
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException

class RefreshTokenAuthenticator(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userPreferences: DataStore<UserPreferences>
) : Authenticator {
    private var retryCounter = 0

    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {

        if (retryCounter > 3) {
            return null
        }

        if (response.body != null) {
            val jsonParser = JsonParser()
            try {
                val jsonElement: JsonElement = jsonParser.parse(response.body!!.string())
                if (jsonElement.isJsonObject) {
                    try {
                        val jsonObject: JsonObject = jsonElement.asJsonObject
                        if (jsonObject.has("message")) {
                            if (!jsonObject.get("message").asString.contains("jwt expired")) {
                                throw IOException(jsonObject.get("message").asString)
                            }
                        }
                    } catch (ex: JsonSyntaxException) {
                        throw IOException("Json syntax exception when getting object")
                    }
                }
            } catch (ex: JsonSyntaxException) {
                throw IOException("Json syntax exception when parsing")
            }

            synchronized(this) {
                retryCounter++

                val refreshToken = runBlocking {
                    userPreferences.data.last().signInPrefs.refreshToken
                }
                Timber.d("mRefreshToken: %s", refreshToken)
                val username = runBlocking {
                    userPreferences.data.last().signInPrefs.username
                }
                val refreshTokenModel = UserRefreshToken(username, refreshToken)
                val userAccessToken: UserAccessToken =
                    authRemoteDataSource.refreshToken(refreshTokenModel).data

                val newAccessToken = "Bearer " + userAccessToken.accessToken

                runBlocking {
                    userPreferences.updateData { currentPrefs ->
                        currentPrefs.toBuilder()
                            .setSignInPrefs(
                                currentPrefs.signInPrefs.toBuilder().setAccessToken(newAccessToken)
                                    .build()
                            ).build()
                    }
                }

                return response.request.newBuilder()
                    .header("Authorization", newAccessToken)
                    .build()
            }
        }

        return null
    }
}