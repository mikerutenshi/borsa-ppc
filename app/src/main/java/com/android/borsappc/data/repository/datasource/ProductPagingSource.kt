package com.android.borsappc.data.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.net.service.ProductService
import com.android.borsappc.di.RetrofitWithoutAuth
import com.android.borsappc.ui.screen.main.DrawerScreens
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class ProductPagingSource  (private val retrofit: Retrofit) : PagingSource<String, ProductListItem>() {
    val query = QueryProductList()
    override suspend fun load(params: LoadParams<String>): PagingSource.LoadResult<String, ProductListItem> {
        val index = params.key
        return try {
            val response = retrofit.create(ProductService::class.java).getProducts(
                query.search,
                index,
                query.limit,
                query.order,
                query.direction
            )

            LoadResult.Page(response.data, null, null)
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
        state.anchorPosition?.let { anchorPos ->
            val anchorIndex = state.closestItemToPosition(anchorPos)
            return anchorIndex.code
        }
    }
}