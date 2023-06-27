package com.x.present

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel(nextReserved: Int) : ViewModel() {
    val next: LiveData<Int>
        get() = _next

    private val _next = MutableLiveData<Int>()

    init {
        _next.value = nextReserved
    }

    fun update() {
        /*var temp: Int
        do {
            temp = (1..5).random()
        } while (temp == _next.value)

        _next.value = temp*/
        _next.value = _next.value?.plus(1)
        if (_next.value == 6){
            _next.value = 1
        }
    }

    fun refresh() {
        _next.value = _next.value
    }
}

class MainViewModelFactory(private val nextReserved: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(nextReserved) as T
    }

}