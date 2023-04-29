package com.android.borsappc.ui.screen.product

import Filter
import PAGE_LIMIT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val products = productRepository
        .getProductListPrefs()
        .flatMapLatest { pref ->
           val queries = QueryProductList(
               gender = pref.gender.ifEmpty { null },
               subCategory = pref.subcategory.ifEmpty { null },
               limit = PAGE_LIMIT,
               orderBy = pref.order.orderByRemote.ifEmpty { Filter.BY_CREATED_AT },
               orderDirection = pref.order.orderDirection.ifEmpty { Filter.DIRECTION_DESC }
           )
            productRepository.getProducts(queries).cachedIn(viewModelScope)
        }
}