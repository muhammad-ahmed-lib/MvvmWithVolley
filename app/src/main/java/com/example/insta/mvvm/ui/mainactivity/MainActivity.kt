package com.example.insta.mvvm.ui.mainactivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.insta.mvvm.helpers.Result
import com.example.insta.mvvm.models.ApiResponseModelClass
import com.example.insta.mvvm.repository.Repository
import com.example.insta.mvvm.ui.theme.MvvmTheme
import com.example.insta.mvvm.utils.newScreen
import com.example.insta.mvvm.viewmodel.MainViewModel
import com.example.insta.mvvm.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()
        val factory = MainViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        setContent {
            MvvmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val TAG="mainInfo"
    val context= LocalContext.current
    val apiResult by viewModel.observeApi.collectAsState()

    when (val result = apiResult) {
        is Result.InProgress -> {
            CircularProgressBarDemo(isLoading = result.isStarted)
        }
        is Result.Success -> {
            Log.d(TAG, "Success: "+result.data.size)
            Column {
                Text(text ="Navigate",modifier=Modifier.clickable {
                    context.newScreen(ViewerActivity::class.java, isFinish = true,
                        clearStack = true)
                })
                LazyColumn(
                    modifier =Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    items(result.data.size) { pos ->
                        RecItemView(model = result.data[pos])
                    }
                }
            }
        }
        is Result.Failure -> {
            Text(text = "Error: ${result.exception}", modifier = modifier)
        }
    }
}
@Composable
fun CircularProgressBarDemo(isLoading: Boolean) {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        if (!isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp),
                color = Color.Blue,
                strokeWidth = 5.dp
            )
        }
    }

}
@Composable
fun RecItemView(model:ApiResponseModelClass){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)){
        Column {
            Text(text =model.title)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
            AsyncImage(
                model = model.thumbnailUrl,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}