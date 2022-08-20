package com.android.borsappc.data.net.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.borsappc.data.model.Work
import com.android.borsappc.data.net.service.WorkService
import com.android.borsappc.di.RetrofitWithAuth
import retrofit2.Retrofit
import javax.inject.Inject

class WorkPagingSource @Inject constructor(
    @RetrofitWithAuth private val retrofit : Retrofit
) : PagingSource<Int, Work>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Work> {
        val nextPage = params.key ?: 1
        val response = retrofit.create(WorkService::class.java).getWorks()
    }

    override fun getRefreshKey(state: PagingState<Int, Work>): Int? {
        TODO("Not yet implemented")
    }
}