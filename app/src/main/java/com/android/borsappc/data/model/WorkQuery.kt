package com.android.borsappc.data.model

import API_DATE_FORMAT
import Filter
import PAGE_LIMIT
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WorkQuery (
    var searchKeyword: String? = null,
    var startDate: String = LocalDate.now().minusWeeks(1L).format(
        DateTimeFormatter.ofPattern(API_DATE_FORMAT)),
    var endDate: String = LocalDate.now().format(
        DateTimeFormatter.ofPattern(API_DATE_FORMAT)),
    var orderDirection: String = Filter.DIRECTION_DESC,
    var orderBy: String = Filter.BY_CREATED_AT,
    var limit: Int = PAGE_LIMIT,
    var page: Int = 1,
)
