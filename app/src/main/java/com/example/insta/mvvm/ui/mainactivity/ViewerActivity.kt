package com.example.insta.mvvm.ui.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.insta.mvvm.ui.mainactivity.ui.theme.MvvmTheme
import com.example.insta.mvvm.utils.newScreen

class ViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MvvmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    val context= LocalContext.current
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    BackHandler {
        context.newScreen(MainActivity::class.java)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MvvmTheme {
        Greeting2("Android")
    }
}