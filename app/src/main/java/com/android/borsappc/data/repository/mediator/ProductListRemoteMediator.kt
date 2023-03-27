package com.android.borsappc.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.android.borsappc.data.model.Order
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.repository.datasource.ProductLocalDataSource
import com.android.borsappc.data.repository.datasource.ProductRemoteDataSource
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProductListRemoteMediator(
    private val queries: QueryProductList,
    private val productLocalDataSource: ProductLocalDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource
) : RemoteMediator<String, ProductListItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, ProductListItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(true)

                    when (queries.orderBy) {
                        Order.By_CREATED_AT -> lastItem.createdAt
                        Order.By_CODE -> lastItem.code
                        Order.By_NAME -> lastItem.name
                        else -> {
                            lastItem.createdAt
                        }
                    }
                }
            }

            queries.index = loadKey
            val response = productRemoteDataSource.getProducts(queries)

            if (loadType == LoadType.REFRESH) {
                productLocalDataSource.clearAll()
            }
            productLocalDataSource.insertAll(response.data)
            val nextKey = response.nextKey

            MediatorResult.Success(endOfPaginationReached = nextKey == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}