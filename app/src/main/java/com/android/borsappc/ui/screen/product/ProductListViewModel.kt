package com.android.borsappc.ui.screen.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.android.borsappc.data.repository.ProductRepository
import javax.inject.Inject

class ProductListViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    val products = productRepository.getProducts().cachedIn(viewModelScope)
}