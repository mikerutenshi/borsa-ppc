package com.android.borsappc.data.net.service

import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("v2/products")
    suspend fun getProducts(
        @Query("filter_gender") gender: String?,
        @Query("filter_subcategory") subcategory: String?,
        @Query("search_key") searchKey: String?,
        @Query("page_index") index: String?,
        @Query("page_limit") limit: Int,
        @Query("order_by") orderBy: String,
        @Query("order_direction") orderDirection: String,
    ): Response<ResponseBody<ArrayList<ProductListItem>>>
}