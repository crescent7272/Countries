package com.hilal.countries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    // coroutine ne yapcak onu soylucez
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main  //isini yap sonra main threade don.


    override fun onCleared() {
        super.onCleared()
        job.cancel()

    }
}