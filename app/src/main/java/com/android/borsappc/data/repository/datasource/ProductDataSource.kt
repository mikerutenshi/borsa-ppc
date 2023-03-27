package com.android.borsappc.data.repository.datasource

import com.android.borsappc.data.model.PagedData
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList

interface ProductDataSource {
    suspend fun getProducts(query: QueryProductList): PagedData<String, ArrayList<ProductListItem>>
}