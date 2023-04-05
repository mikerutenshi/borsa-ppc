package com.android.borsappc.data.repository.datasource

import androidx.sqlite.db.SimpleSQLiteQuery

class QueryBuilder {
    private lateinit var query: SimpleSQLiteQuery
    private lateinit var tableName: String
    private lateinit var orderQuery: String
    private var searchQuery: String? = null
    private var filterQueries = arrayListOf<String>()
    private var whereKeyword: String? = null

    private val queryString = buildString {
        append("SELECT * FROM $tableName")
        whereKeyword?.let {
            append(it)
        }
        if (filterQueries.size > 0) {
            filterQueries.joinToString("AND")
            append(filterQueries)
        }
        searchQuery?.let {
            append(it)
        }
        append(orderQuery)
    }

    fun search(key: String, props: ArrayList<String>) {
        if (whereKeyword == null) whereKeyword = "WHERE"
        searchQuery = buildString {
            props.joinToString(separator = " OR ") { prop ->
                append("$prop LIKE $key")
            }
        }
    }

    fun filterProp(prop: String, value: String) {
        if (whereKeyword == null) whereKeyword = "WHERE"
        val query = "$prop = $value"
        filterQueries.add(query)
    }

    fun order(orderBy: String, orderDirection: String, limit: Int) {
        orderQuery = buildString {
            append("ORDER BY $orderBy ")
            append(orderDirection)
            append("LIMIT $limit")
        }
    }

    fun build(): SimpleSQLiteQuery {
        return SimpleSQLiteQuery(queryString)
    }
}