package com.android.borsappc

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.borsappc.data.repository.AuthRepository
import com.android.borsappc.ui.BorsaPPCTheme
import com.android.borsappc.ui.auth.AuthScreen
import com.android.borsappc.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BorsaPPCTheme {
//                MyApp()
//                SearchBar()
//                Surface(modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
                    AuthScreen(viewModel = authViewModel)
//                }
            }
        }
    }
}

@Composable
private fun MyApp() {
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
    val mNames = List(1000) { "$it" }

    if (shouldShowOnBoarding) {
        OnBoardingScreen(onContinueClicked = { shouldShowOnBoarding = false })
    } else {
        Greetings(mNames)
    }
}

@Composable
private fun Greetings(names: List<String>) {
    androidx.compose.material.Surface(color = MaterialTheme.colors.background, modifier = Modifier.padding(vertical = 4.dp)) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = names) { name ->
                Greeting(name = name)
            }
        }
//        Column() {
//            for (name in names) {
//                Greeting(name)
//            }
//        }
    }
}

@Composable
private fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        elevation = 4.dp) {
        Row(modifier = Modifier
            .animateContentSize(
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
            )
            .padding(12.dp)
        ) {
            Column(modifier = Modifier
                .padding(12.dp)
                .weight(1f)) {
                Text(text = "Hello, ")
                Text(name, fontWeight = FontWeight.Medium, fontSize = 36.sp,
                    modifier = Modifier.padding(8.dp))
                if (expanded) Text(stringResource(R.string.lorem_ipsum_dolor))
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else
                        Icons.Outlined.ExpandMore,
                    contentDescription = if (expanded) {
                        stringResource(id = R.string.show_less)
                    } else {
                        stringResource(id = R.string.show_more)
                    }
                )
            }
//            OutlinedButton(onClick = {
//                expanded = !expanded
//            }) {
//                Text(if (expanded) "Show Less" else "Show More")
//            }
        }
    }
}

@Composable
fun OnBoardingScreen(onContinueClicked: () -> Unit) {
    Surface() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the basic codelab")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = { onContinueClicked() }
            ) {
                Text("Continue")
            }
        }
    }
}

//@Preview(showBackground = true, name = "Text Preview", uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun DefaultPreview() {
//    BorsaPPCTheme() {
//        androidx.compose.material.Surface(color = MaterialTheme.colors.background) {
//            Greetings(names = listOf("Michael", "Jane"))
//        }
//    }
//}
//
//@Preview(showBackground = true, widthDp = 320, heightDp = 320)
//@Composable
//fun OnBoardingPreview() {
//    BorsaPPCTheme {
//        OnBoardingScreen(onContinueClicked = {})
//    }
//}