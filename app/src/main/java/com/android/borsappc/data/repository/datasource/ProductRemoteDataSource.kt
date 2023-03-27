package com.android.borsappc.data.repository.datasource

import com.android.borsappc.data.model.PagedData
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.net.service.ProductService
import com.android.borsappc.di.RetrofitWithoutAuth
import retrofit2.Retrofit
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(@RetrofitWithoutAuth val retrofit: Retrofit) : ProductDataSource {

    override suspend fun getProducts(queries: QueryProductList): PagedData<String, ArrayList<ProductListItem>> {
        val response = retrofit.create(ProductService::class.java).getProducts(
            gender = queries.gender,
            subcategory = queries.subCategory,
            searchKey = queries.searchKey,
            index = queries.index,
            limit = queries.limit,
            orderBy = queries.orderBy.first,
            orderDirection = queries.orderDirection
            /**/
        )

        val nextKey = response.headers().get("Page Next Key")

        return PagedData(data = response.body()!!.data, nextKey = nextKey)
    }
}