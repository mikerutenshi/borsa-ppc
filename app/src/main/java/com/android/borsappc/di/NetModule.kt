package com.android.borsappc.di

import androidx.datastore.core.DataStore
import com.android.borsappc.BuildConfig
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.net.ErrorInterceptor
import com.android.borsappc.data.net.RefreshTokenAuthenticator
import com.android.borsappc.data.net.TokenInterceptor
import com.android.borsappc.data.net.datasource.AuthRemoteDataSource
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
    fun retrofitWithAuth(httpClient: OkHttpClient, gson: Gson): Retrofit {
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
        refreshTokenAuthenticator: RefreshTokenAuthenticator,
        errorInterceptor: ErrorInterceptor
    ): OkHttpClient {
        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.authenticator(refreshTokenAuthenticator)
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
    fun retrofitWithoutAuth(loggingInterceptor: HttpLoggingInterceptor,
        errorInterceptor: ErrorInterceptor): Retrofit {
        //retrofit exclusively for auth services
        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(errorInterceptor)

        if (BuildConfig.DEBUG) {
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
        authRemoteDataSource: Lazy<AuthRemoteDataSource>,
        userPreferences: DataStore<UserPreferences>
    ): ErrorInterceptor {
        return ErrorInterceptor(userPreferences, authRemoteDataSource)
    }

    @Provides
    fun refreshTokenAuthenticator(
        authRemoteDataSource: AuthRemoteDataSource,
        userPreferences: DataStore<UserPreferences>
    ): RefreshTokenAuthenticator {
        return RefreshTokenAuthenticator(authRemoteDataSource, userPreferences)
    }
}