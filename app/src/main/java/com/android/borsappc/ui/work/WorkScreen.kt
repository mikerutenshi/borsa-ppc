package com.android.borsappc.ui.work

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
import cafe.adriel.voyager.androidx.AndroidScreen
import com.android.borsappc.getActivity
import com.android.borsappc.ui.DashboardScaffold
import kotlinx.coroutines.CoroutineScope

class WorkScreen : AndroidScreen() {
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
    val activity = LocalContext.current.getActivity()

    BackHandler() {
        activity?.finish()
    }

    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Work Screen", style = typography.h1)
    }
}
