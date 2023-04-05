package com.android.borsappc.data.repository.datasource

import com.android.borsappc.ProductListPrefs
import com.android.borsappc.data.model.PagedList
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.net.service.ProductService
import com.android.borsappc.di.RetrofitWithoutAuth
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(@RetrofitWithoutAuth val retrofit: Retrofit) : ProductDataSource {

    override suspend fun getProducts(queries: QueryProductList): PagedList<String, ProductListItem> {
        val response = retrofit.create(ProductService::class.java).getProducts(
            gender = queries.gender,
            subcategory = queries.subCategory,
            searchKey = queries.searchKey,
            index = queries.index,
            limit = queries.limit,
            orderBy = queries.orderBy,
            orderDirection = queries.orderDirection
            /**/
        )

        val nextKey = response.headers()["Page Next Key"]

        return PagedList(data = response.body()!!.data, nextKey = nextKey)
    }

    override fun getProductListPrefs(): Flow<ProductListPrefs> {
        TODO("Not yet implemented")
    }

    override suspend fun putProductListPrefs(queries: QueryProductList) {
        TODO("Not yet implemented")
    }
}