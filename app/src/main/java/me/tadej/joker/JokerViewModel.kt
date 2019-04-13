package me.tadej.joker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.tadej.joker.api.FunnyApi
import javax.inject.Inject

class JokerViewModel(private val api: FunnyApi) : ViewModel() {
    private val data = MutableLiveData<String>()
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun data(): LiveData<String> = data

    fun requestNewJoke() {
        scope.launch(Dispatchers.IO) {
            val joke = api.tellJoke()
            if (joke.isNotEmpty()) {
                data.postValue(joke)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    class Factory @Inject constructor(
        private val api: FunnyApi
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST") // As per the guidelines.
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = JokerViewModel(api) as T
    }
}
