package com.android.borsappc.ui.screen.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.hilt.getViewModel
import com.android.borsappc.R
import com.android.borsappc.data.model.ProductListItem
import com.android.borsappc.ui.screen.ErrorItem
import com.android.borsappc.ui.screen.LoadingItem
import com.android.borsappc.ui.screen.LoadingView
import kotlinx.coroutines.flow.Flow

class ProductScreen : AndroidScreen() {

    override val key: ScreenKey = "ProductScreen"

    @Composable
    override fun Content() {
        val viewModel = getViewModel<ProductListViewModel>()
        ProductScreenContent(viewModel)
    }

}

@Composable
fun ProductScreenContent(productListViewModel: ProductListViewModel) {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
//        Text("Product Screen", style = MaterialTheme.typography.h1)
        ProductList(products = productListViewModel.products)
    }
}

@Composable
fun ProductList(products: Flow<PagingData<ProductListItem>>) {
    val lazyProductItems: LazyPagingItems<ProductListItem> = products.collectAsLazyPagingItems()
    LazyColumn(contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()) {
        items(lazyProductItems) { productItem ->
            productItem?.let {
                ProductItem(product = productItem)
            }
        }

        lazyProductItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = lazyProductItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = lazyProductItems.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductItem(product: ProductListItem) {
    Card({}, Modifier.padding(bottom = 16.dp)) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_article))
                Text(product.code)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_name))
                Text(product.name)
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(id = R.string.label_subcat))
                Text(product.subCategory)
            }
        }
    }
}