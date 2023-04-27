package com.android.borsappc.data.model

import Filter
import PAGE_LIMIT

data class QueryProductList(
    var gender: String? = null,
    var subCategory: String? = null,
    override var limit: Int = PAGE_LIMIT,
    override var index: String? = null,
    override var orderBy: String = Filter.BY_CREATED_AT,
    override var orderDirection : String = Filter.DIRECTION_DESC,
    override var searchKey : String? = null
) : Query