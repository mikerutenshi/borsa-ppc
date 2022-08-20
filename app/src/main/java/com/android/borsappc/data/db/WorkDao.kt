package com.android.borsappc.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.borsappc.data.model.Work

@Dao
interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(works: List<Work>)

    @Query("SELECT * FROM works")
    fun pagingSource(): PagingSource<Int, Work>

    @Query("DELETE FROM works")
    suspend fun clearAll()
}