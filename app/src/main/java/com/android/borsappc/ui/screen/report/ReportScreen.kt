package com.android.borsappc.ui.screen.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.screen.ScreenKey

class ReportScreen : AndroidScreen() {

    override val key: ScreenKey = "ReportScreen"

    @Composable
    override fun Content() {
        ReportScreenContent()
    }

}

@Composable
fun ReportScreenContent() {
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Report Screen", style = typography.h1)
    }
}
