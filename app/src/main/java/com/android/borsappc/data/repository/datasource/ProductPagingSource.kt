package com.android.borsappc.data.repository.datasource

import Filter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource  (private val queries: QueryProductList, private val localDataSource: ProductLocalDataSource) : PagingSource<String, ProductListItem>() {

    override suspend fun load(params: LoadParams<String>): PagingSource.LoadResult<String, ProductListItem> {
        val index = params.key
        queries.index = index

        return try {
            val pagedList = localDataSource.getProducts(queries)

            PagingSource.LoadResult.Page(pagedList.data, null, pagedList.nextKey)
        }
        catch (e: IOException) {
            // IOException for network failures.
            return PagingSource.LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return PagingSource.LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<String, ProductListItem>): String? {
        var refreshKey: String? = null

        state.anchorPosition?.let { anchorPos ->
            state.closestItemToPosition(anchorPos)?.let {
                refreshKey = when (queries.orderBy) {
                    Filter.BY_CREATED_AT -> it.createdAt
                    Filter.BY_CODE -> it.code
                    Filter.BY_NAME -> it.name
                    else -> {
                        it.createdAt
                    }
                }
            }
        }
        return refreshKey
    }
}