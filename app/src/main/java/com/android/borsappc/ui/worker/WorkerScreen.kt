package com.android.borsappc.ui.worker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.android.borsappc.getActivity
import com.android.borsappc.ui.DashboardScaffold

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkerScreen() {
    val drawerState = rememberBottomDrawerState(initialValue = BottomDrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val activity = LocalContext.current.getActivity()

    BackHandler() {
        activity?.finish()
    }

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Worker Screen", style = typography.h1)
    }
}
