package com.android.borsappc.di

import androidx.datastore.core.DataStore
import com.android.borsappc.BuildConfig
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.net.ErrorInterceptor
import com.android.borsappc.data.net.TokenAuthenticator
import com.android.borsappc.data.net.TokenInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetModule {
    @RetrofitWithAuth
    @Provides
    fun apiRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    @Provides
    fun httpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: TokenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.authenticator(tokenAuthenticator)
        httpClientBuilder.addInterceptor(errorInterceptor)
        httpClientBuilder.addInterceptor(tokenInterceptor)
        if (BuildConfig.DEBUG) {
            if (!httpClientBuilder.interceptors().contains(loggingInterceptor))
                httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return httpClientBuilder.build()
    }

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    fun tokenInterceptor(userPreferences: DataStore<UserPreferences>): TokenInterceptor {
        return TokenInterceptor(userPreferences)
    }

    @RetrofitWithoutAuth
    @Provides
    fun tokenRetrofit(errorInterceptor: ErrorInterceptor): Retrofit {
        //retrofit exclusively for token refresh
        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(errorInterceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (!httpClientBuilder.interceptors()
                    .contains(loggingInterceptor)
            ) httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder.build())
            .build()
    }

    @Provides
    fun errorInterceptor(
        @RetrofitWithoutAuth retrofit: Lazy<Retrofit>,
        userPreferences: DataStore<UserPreferences>
    ): ErrorInterceptor {
        return ErrorInterceptor(userPreferences, retrofit)
    }

    @Provides
    fun tokenAuthenticator(
        @RetrofitWithoutAuth retrofit: Lazy<Retrofit>,
        userPreferences: DataStore<UserPreferences>
    ): TokenAuthenticator {
        return TokenAuthenticator(retrofit, userPreferences)
    }
}