package com.android.borsappc.data.model

import java.time.LocalDate

data class WorkQuery(
    val searchKeyword: String? = null,
    val startDate: String = LocalDate.parse("yyyy-MM-dd").minusWeeks(1L).toString(),
    val endDate: String = LocalDate.parse("yyyy-MM-dd").toString(),
    val sortDirection: String = "asc",
    val sortBy: String = "spk_no",
    val limit: Int = 10,
    val page: Int = 1,
)
