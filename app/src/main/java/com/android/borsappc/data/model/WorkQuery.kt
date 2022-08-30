package com.android.borsappc.data.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class WorkQuery(
    val searchKeyword: String? = null,
    val startDate: String = LocalDate.now().minusWeeks(1L).format(
        DateTimeFormatter.ofPattern(API_DATE_FORMAT)),
    val endDate: String = LocalDate.now().format(
        DateTimeFormatter.ofPattern(API_DATE_FORMAT)),
    val sortDirection: String = "asc",
    val sortBy: String = "spk_no",
    val limit: Int = 10,
    val page: Int = 1,
)
