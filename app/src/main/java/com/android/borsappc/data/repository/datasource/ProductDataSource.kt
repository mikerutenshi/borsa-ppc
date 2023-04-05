package com.android.borsappc.data.repository.datasource

import com.android.borsappc.ProductListPrefs
import com.android.borsappc.data.model.PagedList
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import kotlinx.coroutines.flow.Flow

interface ProductDataSource {
    suspend fun getProducts(queries: QueryProductList): PagedList<String, ProductListItem>
    fun getProductListPrefs(): Flow<ProductListPrefs>
    suspend fun putProductListPrefs(queries: QueryProductList)
}