package com.br.brqinvestimentos.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext by lazy {
        AppContextProvider.io + job
    }

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

}

object AppContextProvider : CoroutineContextProvider {
    override val main: CoroutineContext = Dispatchers.Unconfined
    override val io: CoroutineContext = Dispatchers.Unconfined
}

interface CoroutineContextProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}