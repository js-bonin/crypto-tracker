package com.jsbonin.ethereumtracker.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsbonin.ethereumtracker.ui.theme.EthereumTrackerTheme

@Composable
fun MainView() {
    EthereumTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Greeting("Android")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EthereumTrackerTheme {
        Greeting("Android")
    }
}