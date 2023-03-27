package com.android.borsappc.data.db

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.android.borsappc.data.model.ProductListItem

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(items: List<ProductListItem>)

    @RawQuery(observedEntities = [ProductListItem::class])
     fun pagingProductList(querySql: SimpleSQLiteQuery): PagingSource<String, ProductListItem>

    @Query("DELETE FROM products")
     suspend fun clearall()
}