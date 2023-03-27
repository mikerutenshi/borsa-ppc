package com.android.borsappc.data.repository.datasource

import androidx.paging.PagingSource
import com.android.borsappc.data.db.AppDatabase
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.data.model.QueryProductList
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(private val database: AppDatabase) {
    private val productDao = database.productDao()

    fun getProducts(queries: QueryProductList): PagingSource<String, ProductListItem> {

        val searchKey = queries.searchKey
        val gender = queries.gender
        val subcategory = queries.subCategory
        val orderBy = queries.orderBy.second
        val direction = queries.orderDirection
        val sqlBuilder = QueryBuilder()

        gender?.let { sqlBuilder.filterProp("gender", it) }
        subcategory?.let { sqlBuilder.filterProp("subcategory", it) }
        searchKey?.let {
            val props = arrayListOf<String>("code", "name")
            sqlBuilder.search(searchKey, props)
        }
        sqlBuilder.order(orderBy, direction)

        val querySql = sqlBuilder.build()
        val data = productDao.pagingProductList(querySql)

        return data
    }

    suspend fun insertAll(data: ArrayList<ProductListItem>) {
        productDao.insertAll(data)
    }

    suspend fun clearAll() {
        productDao.clearall()
    }
}