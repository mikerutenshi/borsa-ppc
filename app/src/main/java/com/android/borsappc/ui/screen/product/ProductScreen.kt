package com.android.borsappc.ui.screen.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey

class ProductScreen : AndroidScreen() {

    override val key: ScreenKey = "ProductScreen"

    @Composable
    override fun Content() {
        ProductScreenContent()
    }

}

@Composable
fun ProductScreenContent() {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Product Screen", style = typography.h1)
    }
}
