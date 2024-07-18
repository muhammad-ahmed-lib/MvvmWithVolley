package com.example.insta.mvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.insta.mvvm.helpers.Result
import com.example.insta.mvvm.models.ApiResponseModelClass
import com.example.insta.mvvm.repository.Repository

class MainViewModel(application: Application, private val rep: Repository) : AndroidViewModel(application) {
    private val _observeApi = MutableStateFlow<Result<List<ApiResponseModelClass>>>(Result.InProgress(isStarted = false))
    val observeApi: StateFlow<Result<List<ApiResponseModelClass>>> get() = _observeApi
    private val TAG="mainViewModelInfo"
    init {
        viewModelScope.launch {
            _observeApi.value= rep.fetchPosts(application.applicationContext)
            Log.d(TAG, "init"+ rep.fetchPosts(application.applicationContext))
        }
    }
}
