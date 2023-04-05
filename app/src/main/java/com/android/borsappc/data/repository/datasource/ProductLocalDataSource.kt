package com.android.borsappc.data.repository.datasource

import androidx.datastore.core.DataStore
import com.android.borsappc.Order
import com.android.borsappc.ProductListPrefs
import com.android.borsappc.UserPreferences
import com.android.borsappc.data.db.AppDatabase
import com.android.borsappc.data.model.Filter
import com.android.borsappc.data.model.PagedList
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    database: AppDatabase,
    private val userPrefs: DataStore<UserPreferences>
) : ProductDataSource {
    private val productDao = database.productDao()

    override suspend fun getProducts(queries: QueryProductList): PagedList<String, ProductListItem> {
        val index = queries.index
        val searchKey = queries.searchKey
        val gender = queries.gender
        val subcategory = queries.subCategory
        val orderBy = queries.orderBy
        val direction = queries.orderDirection
        val limit = queries.limit + 1
        val sqlBuilder = QueryBuilder()

        index?.let { sqlBuilder.filterProp(orderBy, it)}
        gender?.let { sqlBuilder.filterProp("gender", it) }
        subcategory?.let { sqlBuilder.filterProp("subcategory", it) }
        searchKey?.let {
            val props = arrayListOf("code", "name")
            sqlBuilder.search(searchKey, props)
        }
        sqlBuilder.order(orderBy, direction, limit)

        val querySql = sqlBuilder.build()
        val products = productDao.getProducts(querySql)
        val nextItem = products.lastOrNull()
        var nextKey: String? = null
        nextItem?.let {
            nextKey = when (queries.orderBy) {
                Filter.BY_CREATED_AT -> it.createdAt
                Filter.BY_CODE -> it.code
                Filter.BY_NAME -> it.name
                else -> {
                    it.createdAt
                }
            }
        }
        products.dropLast(1)

        return PagedList(ArrayList(products), nextKey)
    }

    override fun getProductListPrefs(): Flow<ProductListPrefs> {
        return userPrefs.data.map {  userPrefs ->
            userPrefs.productListPrefs
        }
    }

    override suspend fun putProductListPrefs(queries: QueryProductList) {
        userPrefs.updateData {
            UserPreferences.newBuilder().setProductListPrefs(
                ProductListPrefs.newBuilder()
                    .setGender(queries.gender)
                    .setOrder(
                        Order.newBuilder()
                            .setOrderByRemote(queries.orderBy)
                            .setOrderDirection(queries.orderDirection)
                            .build()
                    )
                    .build()
            ).build()
        }
    }

    suspend fun insertAll(data: ArrayList<ProductListItem>) {
        productDao.insertAll(data)
    }

    suspend fun clearAll() {
        productDao.clearAll()
    }
}