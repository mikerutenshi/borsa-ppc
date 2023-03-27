package com.android.borsappc.data.db

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.android.borsappc.data.model.Work
import com.android.borsappc.data.model.WorkQuery

@Dao
abstract class WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(works: List<Work>)

    @RawQuery(observedEntities = [Work::class])
    abstract fun pagingSource(query: SimpleSQLiteQuery): PagingSource<Int, Work>

    @Query("DELETE FROM works")
    abstract suspend fun clearAll()

    fun getPagingSource(query: WorkQuery): PagingSource<Int, Work> {
        return pagingSource(buildQuery(query))
    }

    private fun buildQuery(query: WorkQuery): SimpleSQLiteQuery {
        val sortKey = query.sortBy.second
        val sortDirection = if (query.sortDirection == "asc") {
            "ASC"
        } else {
            "DESC"
        }
        val args = arrayListOf<Any>()
        val queryString = buildString {
            append("SELECT * FROM works " /*ORDER BY :sortBy ASC"*/)

            query.searchKeyword?.let {
                append("WHERE spkNo LIKE %?% OR articleNo LIKE %?% ")
                args.add(it)
            }
            append("ORDER BY $sortKey ")
            append(sortDirection)
        }

        return SimpleSQLiteQuery(queryString, args.toArray())
    }
}