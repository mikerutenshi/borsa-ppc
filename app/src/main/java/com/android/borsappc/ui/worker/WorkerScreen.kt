package com.android.borsappc.ui.worker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey

class WorkerScreen : AndroidScreen() {

    override val key: ScreenKey = "WorkerScreen"

    @Composable
    override fun Content() {
        WorkerScreenContent()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkerScreenContent() {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Worker Screen", style = typography.h1)
    }

}
