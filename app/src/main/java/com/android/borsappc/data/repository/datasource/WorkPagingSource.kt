package com.android.borsappc.data.repository.datasource

//class WorkPagingSource @Inject constructor(
//    @RetrofitWithAuth private val retrofit : Retrofit
//) : PagingSource<Int, Work>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Work> {
//        val nextPage = params.key ?: 1
//        val response = retrofit.create(WorkService::class.java).getWorks()
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Work>): Int? {
//        TODO("Not yet implemented")
//    }
//}