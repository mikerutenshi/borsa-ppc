package com.android.borsappc.ui.screen.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.repository.WorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkViewModel @Inject constructor(
    private val workRepository: WorkRepository
) : ViewModel() {
    val works = workRepository.getWorks(WorkQuery()).cachedIn(viewModelScope)
}