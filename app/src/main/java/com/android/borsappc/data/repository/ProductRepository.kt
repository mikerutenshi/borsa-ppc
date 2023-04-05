package com.android.borsappc.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.android.borsappc.ProductListPrefs
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.repository.datasource.ProductLocalDataSource
import com.android.borsappc.data.repository.datasource.ProductPagingSource
import com.android.borsappc.data.repository.datasource.ProductRemoteDataSource
import com.android.borsappc.data.repository.mediator.ProductListRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor (
    private val productLocalDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productPagingSource: ProductPagingSource
    ) {
    @OptIn(ExperimentalPagingApi::class)
    fun getProducts(queries: QueryProductList): Flow<PagingData<ProductListItem>> {
        val config = PagingConfig(queries.limit)
        return Pager(
            config = config,
            remoteMediator = ProductListRemoteMediator(
            queries,
            productLocalDataSource,
            productRemoteDataSource)
        ) {
            productPagingSource
        }.flow
    }

    fun getProductListPrefs(): Flow<ProductListPrefs> {
        return productLocalDataSource.getProductListPrefs()
    }

    suspend fun putProductListPrefs(queries: QueryProductList) {
        productLocalDataSource.putProductListPrefs(queries)
    }
}