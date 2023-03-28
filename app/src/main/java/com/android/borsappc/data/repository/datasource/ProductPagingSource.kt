package com.android.borsappc.data.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.borsappc.data.model.HEADER_PAGE_NEXT_KEY
import com.android.borsappc.data.model.Order
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.net.service.ProductService
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException

class ProductPagingSource  (private val queries: QueryProductList, private val retrofit: Retrofit) : PagingSource<String, ProductListItem>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, ProductListItem> {
        val index = params.key
        return try {
            val response = retrofit.create(ProductService::class.java).getProducts(
                queries.gender,
                queries.subCategory,
                queries.searchKey,
                index,
                queries.limit,
                queries.orderBy.first,
                queries.orderDirection
            )

            val nextKey = response.headers()[HEADER_PAGE_NEXT_KEY]
            LoadResult.Page(response.body()!!.data, null, nextKey)
        }
        catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<String, ProductListItem>): String? {
        var refreshKey: String? = null
        state.anchorPosition?.let { anchorPos ->
            state.closestItemToPosition(anchorPos)?.let {
                refreshKey = when (queries.orderBy) {
                    Order.By_CREATED_AT -> it.createdAt
                    Order.By_CODE -> it.code
                    Order.By_NAME -> it.name
                    else -> {
                        it.createdAt
                    }
                }
            }
        }
        return refreshKey
    }
}