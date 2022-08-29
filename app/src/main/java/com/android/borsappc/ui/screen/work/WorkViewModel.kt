package com.android.borsappc.ui.screen.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.repository.WorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val workRepository: WorkRepository
) : ViewModel() {
    private val queryFlow = workRepository.getWorkFilterData()
    @OptIn(ExperimentalCoroutinesApi::class)
    val works = queryFlow.flatMapLatest {
        val workQuery = WorkQuery(
            startDate = it.date.startDate,
            endDate = it.date.endDate,
            sortBy = it.sort.sortBy,
            sortDirection = it.sort.sortDirection
        )
        workRepository.getWorks(workQuery).cachedIn(viewModelScope)
    }
}