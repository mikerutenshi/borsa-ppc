package com.android.borsappc.data.model

open class Query  (
    var limit: Int = 10,
    var index: String?,
    var orderBy: Pair<String, String>,
    var orderDirection : String,
    var searchKey : String?
)