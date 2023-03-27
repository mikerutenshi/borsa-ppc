package com.android.borsappc.data.repository.datasource

import androidx.sqlite.db.SimpleSQLiteQuery

class QueryBuilder {
    private lateinit var query: SimpleSQLiteQuery
    private lateinit var tableName: String
    private lateinit var orderQuery: String
    private var searchQuery: String? = null
    private var filterQuery: String? = null
    private var whereKeyword: String? = null

    private val queryString = buildString {
        append("SELECT * FROM $tableName")
        whereKeyword?.let {
            append(whereKeyword)
        }
        searchQuery?.let {
            append(searchQuery)
        }
        append(orderQuery)
    }

    fun search(key: String, props: ArrayList<String>) {
        whereKeyword?.let { it = "WHERE" }
        searchQuery = buildString {
            props.joinToString(separator = " OR ") { prop ->
                append("$prop LIKE $key")
            }
        }
    }

    fun filterProp(prop: String, value: String) {
        whereKeyword?.let { it = "WHERE" }
        filterQuery = "$prop = $value"
    }

    fun order(orderBy: String, orderDirection: String) {
        orderQuery = buildString {
            append("ORDER BY $orderBy ")
            append(orderDirection)
        }
    }

    fun build(): SimpleSQLiteQuery {
        return SimpleSQLiteQuery(queryString)
    }
}