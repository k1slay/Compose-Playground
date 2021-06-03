package com.k2.composeplayground.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.k2.composeplayground.MainActivity
import com.k2.composeplayground.R
import com.k2.composeplayground.ui.theme.ComposePlaygroundTheme
import com.k2.composeplayground.ui.theme.Typography

@Composable
fun MakeNavUi(items: List<String>, onClick: () -> Unit) {
    Surface(color = MaterialTheme.colors.background) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "list") {
            composable("list") {
                MakeList(items, navController)
            }
            composable(MainActivity.list[0]) {
                NightSky(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick.invoke() }
                )
            }
            composable(MainActivity.list[1]) {
                Waves(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick.invoke() }
                )
            }
        }
    }
}

@Composable
fun MakeList(items: List<String>, navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = LocalContext.current.getString(R.string.app_name),
            style = Typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.size(24.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(item)
                        }
                ) {
                    Text(
                        text = item,
                        style = Typography.body1,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePlaygroundTheme {
        MakeNavUi(listOf("Item 1, Item 2")) {}
    }
}
