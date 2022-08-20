package com.android.borsappc.data.net.datasource

import androidx.paging.PagingSource
import com.android.borsappc.data.model.UserSignIn
import com.android.borsappc.data.model.Work

interface WorkDataSource {
    suspend fun getWorks(signIn: UserSignIn): PagingSource<Int, Work>
}