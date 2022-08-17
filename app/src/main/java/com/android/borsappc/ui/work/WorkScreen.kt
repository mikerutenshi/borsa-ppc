package com.android.borsappc.ui.work

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey

class WorkScreen : AndroidScreen() {

//    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        WorkScreenContent()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkScreenContent() {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Work Screen", style = typography.h1)
    }
}
