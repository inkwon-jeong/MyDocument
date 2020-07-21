package com.example.android.dagger.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.dagger.user.UserManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SplashViewModel @Inject constructor(
    private val userManager: UserManager
) : CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    fun isUserLoggedIn() = userManager.isUserLoggedIn()
    fun isUserRegistered() = userManager.isUserRegistered()

    private val _isReady = MutableLiveData<Boolean>()
    val isReady: LiveData<Boolean>
        get() = _isReady

    fun load() {
        launch {
            delay(1000)
            _isReady.value = true
        }
    }

    fun doneLoading() {
        _isReady.value = null
    }
}