package com.jsbonin.ethereumtracker.ui

import android.app.Application
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.jsbonin.ethereumtracker.ui.theme.EthereumTrackerTheme
import com.jsbonin.ethereumtracker.viewmodel.MainViewModel

@Composable
fun MainView(viewModel: MainViewModel = MainViewModel(LocalContext.current.applicationContext as Application)) {
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