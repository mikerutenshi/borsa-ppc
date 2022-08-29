package com.android.borsappc.ui.screen.main

import java.time.LocalDate

sealed class MainUIEvent {
    data class StartDateChanged(val date: LocalDate) : MainUIEvent()
    data class EndDateChanged(val date: LocalDate) : MainUIEvent()
    data class SortKeyChanged(val key: String) : MainUIEvent()
    data class SortDirectionChanged(val direction: String) : MainUIEvent()
    object FilterScreenClosed : MainUIEvent()
}
