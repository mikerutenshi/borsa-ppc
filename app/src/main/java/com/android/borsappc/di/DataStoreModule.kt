package com.android.borsappc.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.android.borsappc.SignInPrefs
import com.android.borsappc.UserPreferences
import com.google.protobuf.InvalidProtocolBufferException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

private const val WORKFLOW_PREFERENCES = "workflow_preferences"
private const val DATA_STORE_FILE_NAME = "user_prefs.pb"

private const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN";
private const val PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN";
private const val PREF_ROLE = "PREF_ROLE";
private const val PREF_FIRST_NAME = "PREF_FIRST_NAME";
private const val PREF_LAST_NAME = "PREF_LAST_NAME";
private const val PREF_USERNAME = "PREF_USERNAME";
private const val PREF_WORKER_FILTER_STATUS = "PREF_WORKER_FILTER_STATUS";
private const val PREF_WORK_FILTER_STATUS = "PREF_WORK_FILTER_STATUS";
private const val PREF_WORK_ASSIGN_FILTER_STATUS = "PREF_WORK_ASSIGN_FILTER_STATUS";
private const val PREF_WORK_DO_FILTER_STATUS = "PREF_WORK_DO_FILTER_STATUS";
private const val PREF_WORK_DONE_FILTER_STATUS = "PREF_WORK_DONE_FILTER_STATUS";
private const val PREF_WORK_ASSIGNED_FILTER_STATUS = "PREF_WORK_ASSIGNED_FILTER_STATUS";

@InstallIn(SingletonComponent::class)
@Module
object  DataStoreModule {

    @Singleton
    @Provides
    fun provideProtoDataStore(@ApplicationContext appContext: Context): DataStore<UserPreferences> {
        return DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { appContext.dataStoreFile(DATA_STORE_FILE_NAME) },
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { UserPreferences.getDefaultInstance() }
            ),
            migrations = listOf(
                SharedPreferencesMigration(
                    appContext,
                    WORKFLOW_PREFERENCES
                ) {
                    sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
                        currentData.toBuilder()
                            .setSignInPrefs(SignInPrefs.newBuilder()
                                .setAccessToken(sharedPrefs.getString(PREF_ACCESS_TOKEN, ""))
                                .setRefreshToken(sharedPrefs.getString(PREF_REFRESH_TOKEN, ""))
                                .setRole(sharedPrefs.getString(PREF_ROLE, ""))
                                .setFirstName(sharedPrefs.getString(PREF_FIRST_NAME, ""))
                                .setLastName(sharedPrefs.getString(PREF_LAST_NAME, ""))
                                .setUsername(sharedPrefs.getString(PREF_USERNAME, ""))
                            ).build()
                }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    object UserPreferencesSerializer : Serializer<UserPreferences> {

        override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): UserPreferences {
            try {
                return UserPreferences.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                throw CorruptionException("Cannot read proto.", exception)
            }
        }

        override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
            t.writeTo(output)
    }
}