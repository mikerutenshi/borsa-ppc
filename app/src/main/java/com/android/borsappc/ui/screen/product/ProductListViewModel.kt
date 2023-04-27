package com.android.borsappc.ui.screen.product

import PAGE_LIMIT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.borsappc.data.model.QueryProductList
import com.android.borsappc.data.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ProductListViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val products = productRepository
        .getProductListPrefs()
        .flatMapLatest { pref ->
           val queries = QueryProductList(
               gender = pref.gender,
               subCategory = pref.subcategory,
               limit = PAGE_LIMIT,
               orderBy = pref.order.orderByRemote,
               orderDirection = pref.order.orderDirection
           )
            productRepository.getProducts(queries).cachedIn(viewModelScope)
        }
}