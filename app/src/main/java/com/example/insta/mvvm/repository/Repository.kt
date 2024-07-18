package com.example.insta.mvvm.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.insta.mvvm.helpers.MySingleton
import com.example.insta.mvvm.helpers.Result
import com.example.insta.mvvm.models.ApiResponseModelClass
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class Repository {
    private val TAG = "repInfo"

    suspend fun fetchPosts(context: Context): Result<List<ApiResponseModelClass>> {
        Log.d(TAG, "fetchPosts: Start")
        val url = "https://jsonplaceholder.typicode.com/photos"
        return suspendCancellableCoroutine { continuation ->
            val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    val posts = mutableListOf<ApiResponseModelClass>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val post = ApiResponseModelClass(
                            albumId = jsonObject.getInt("albumId"),
                            id = jsonObject.getInt("id"),
                            title = jsonObject.getString("title"),
                            url = jsonObject.getString("url"),
                            thumbnailUrl = jsonObject.getString("thumbnailUrl")
                        )
                        posts.add(post)
                    }
                    Log.d(TAG, "fetchPosts: $posts")
                    if (continuation.isActive) {
                        continuation.resume(Result.Success(data = posts))
                    }
                },
                { error ->
                    if (continuation.isActive) {
                        continuation.resume(Result.Failure(exception = error.message ?: "An error occurred"))
                    }
                }
            )
            MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest)
            continuation.invokeOnCancellation {
                Log.d(TAG, "fetchPosts: Request cancelled")
                jsonArrayRequest.cancel()
            }
        }
    }
}
