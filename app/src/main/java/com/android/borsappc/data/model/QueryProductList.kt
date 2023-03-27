package com.android.borsappc.data.model

data class QueryProductList(
    var gender: String? = "Female",
    var subCategory: String? = "Mid-cut Boot",
) : Query()