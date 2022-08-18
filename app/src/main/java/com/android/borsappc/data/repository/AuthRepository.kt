package com.android.borsappc.data.repository

import androidx.datastore.core.DataStore
import com.android.borsappc.SignInPrefs
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.model.User
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.net.datasource.AuthRemoteDataSource
import com.android.borsappc.resultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userPrefs: DataStore<UserPreferences>
) {
//    fun signIn(signIn: UserSignIn): Flow<User> {
//        return flow<User> {
//            val data = authRemoteDataSource.signIn(signIn).data
////            val data = User(1, "Michael", "Password", "Michael", "Susanto", "Student",
////            "AccessToken", "RefreshToken")
//            Timber.d("data triggered ${data.username}")
//            emit(data)
//        }.flowOn(Dispatchers.IO)
//    }

    suspend fun storeSignInData(user: User): Result<Unit> {

        return resultOf {
            userPrefs.updateData { currentPrefs ->
                Timber.d("AuthRepo storeSignInData ${currentPrefs.signInPrefs}")
                Timber.d("AuthRepo storeSignInData user $user")
                val signInPrefs = currentPrefs.signInPrefs.newBuilderForType()
                    .setUsername(user.username)
                    .setFirstName(user.firstName)
                    .setLastName(user.lastName ?: "")
                    .setRole(user.role)
                    .setRefreshToken(user.refreshToken)
                    .setAccessToken(user.accessToken)
                    .build()
                Timber.d("AuthRepo storeSignInData signInPrefs $signInPrefs")
                currentPrefs.toBuilder().setSignInPrefs(signInPrefs).build()
            }
        }
    }

    suspend fun getSignInData(): Result<User> {
        val signIn = userPrefs.data.first().signInPrefs

        return resultOf {
            User(
                username = signIn.username,
                firstName = signIn.firstName,
                lastName = signIn.lastName,
                role = signIn.role,
                accessToken = signIn.accessToken,
                refreshToken = signIn.refreshToken)
        }
    }

    suspend fun clearSignInData(): Result<Unit> {
        return resultOf {
            userPrefs.updateData { currentPrefs ->
                val signInPrefs = currentPrefs.signInPrefs.newBuilderForType()
                    .setUsername("")
                    .setFirstName("")
                    .setLastName("")
                    .setRole("")
                    .setRefreshToken("")
                    .setAccessToken("")
                    .build()

                currentPrefs.toBuilder().setSignInPrefs(signInPrefs).build()
            }
        }
    }

    suspend fun signIn(signIn: UserSignIn): Result<User> =
        resultOf {
            withContext(Dispatchers.IO) {
                authRemoteDataSource.signIn(signIn).data
            }
        }

    suspend fun signOut(username: String): Result<Unit> =
        resultOf {
            withContext(Dispatchers.IO) {
                authRemoteDataSource.signOut(username)
            }
        }
}