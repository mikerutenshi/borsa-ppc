package com.android.borsappc.data.model

data class QueryProductList(
    var gender: String?,
    var subCategory: String?,
    override var limit: Int,
    override var index: String? = null,
    override var orderBy: String,
    override var orderDirection : String,
    override var searchKey : String? = null
) : Query