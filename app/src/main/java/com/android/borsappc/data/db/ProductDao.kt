package com.android.borsappc.data.db

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.android.borsappc.data.model.ProductListItem

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAll(items: ArrayList<ProductListItem>)

    @RawQuery(observedEntities = [ProductListItem::class])
     fun getProducts(querySql: SimpleSQLiteQuery): ArrayList< ProductListItem>

    @Query("DELETE FROM products")
     suspend fun clearAll()
}