package com.android.borsappc.ui.screen.main

import API_DATE_FORMAT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.model.WorkQuery
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.data.repository.ProductRepository
import com.android.borsappc.data.repository.WorkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter.ofPattern
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val workRepository: WorkRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    private val _events = MutableSharedFlow<MainScreenEvent>()
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), MainUiState())
    val events = _events.shareIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L))

    init {
        initQueriesFromDataStore()
    }

    private fun initQueriesFromDataStore() {
        viewModelScope.launch {
            workRepository.getWorkFilterData().collectLatest { workFilterPrefs ->
                val workQueryState = WorkQuery()
                val startDate = workFilterPrefs.date.startDate.ifEmpty { null }
                val endDate = workFilterPrefs.date.endDate.ifEmpty { null }
                val orderBy = workFilterPrefs.order.orderByRemote.ifEmpty { null }
                val orderDirection = workFilterPrefs.order.orderDirection.ifEmpty { null }
                startDate?.let { workQueryState.startDate = it }
                endDate?.let { workQueryState.endDate = it }
                orderBy?.let { workQueryState.orderBy = it }
                orderDirection?.let { workQueryState.orderDirection = it }

                _uiState.update {
                    it.copy(
                        workQuery = workQueryState
                    )
                }
                workRepository.storeWorkFilterData(workQueryState)
            }

            productRepository.getProductListPrefs().collectLatest { productListPrefs ->
                val productQueryState = QueryProductList()
                val gender = productListPrefs.gender.ifEmpty { null }
                val subCategory = productListPrefs.subcategory.ifEmpty { null }
                val orderBy = productListPrefs.order.orderByRemote.ifEmpty { null }
                val orderDirection = productListPrefs.order.orderDirection.ifEmpty { null }
                gender?.let { productQueryState.gender = it }
                subCategory?.let { productQueryState.subCategory = it }
                orderBy?.let { productQueryState.orderBy = it }
                orderDirection?.let { productQueryState.orderDirection = it }

                _uiState.update {
                    it.copy(queryProductList = productQueryState)
                }
                productRepository.putProductListPrefs(productQueryState)
            }

        }
    }

    fun onEvent(event: MainUIEvent) {
        when (event) {
            is MainUIEvent.StartDateChanged -> {
                val date = event.date.format(ofPattern(API_DATE_FORMAT))
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(startDate = date)
                    )
                }
            }
            is MainUIEvent.EndDateChanged -> {
                val date = event.date.format(ofPattern(API_DATE_FORMAT))
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(endDate = date)
                    )
                }
            }
            is MainUIEvent.SortKeyChanged -> {
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(orderBy = event.key)
                    )
                }
            }
            is MainUIEvent.SortDirectionChanged -> {
                _uiState.update {
                    it.copy(
                        workQuery =
                        it.workQuery.copy(orderDirection = event.direction)
                    )
                }
            }
            is MainUIEvent.FilterScreenClosed -> {
                viewModelScope.launch {
                    workRepository.storeWorkFilterData(_uiState.value.workQuery)
                }
            }
        }
    }

    fun setCurrentScreen(route: String) {
        _uiState.update {
            it.copy(currentScreen = route)
        }
    }

    fun navigateTo(destination: String) {
        viewModelScope.launch {
            _events.emit(MainScreenEvent.NavigateTo(destination))
        }
    }

     fun signOut() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSigningOut = true)
            }
            authRepository.getSignInData()
                .onSuccess { user ->
                    authRepository.signOut(user.username)
                        .onSuccess {
                            authRepository.clearSignInData()
                                .onSuccess {
                                    _uiState.update {
                                        it.copy(isSigningOut = false)
                                    }
                                    _events.emit(MainScreenEvent.SignOut)
                                }
                                .onFailure { error ->
                                    _uiState.update {
                                        it.copy(isSigningOut = false)
                                    }
                                    error.localizedMessage?.let {
                                        _events.emit(MainScreenEvent.ShowSnackbar(it))
                                    }
                                }
                        }
                        .onFailure { error ->
                            error.localizedMessage?.let {
                                _events.emit(MainScreenEvent.ShowSnackbar(it))
                            }
                        }

                }
                .onFailure { error ->
                    error.localizedMessage?.let {
                        _events.emit(MainScreenEvent.ShowSnackbar(it))
                    }
                }
        }
    }
}