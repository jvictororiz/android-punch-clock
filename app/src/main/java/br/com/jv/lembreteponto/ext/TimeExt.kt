package br.com.jv.lembreteponto.ext

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.waitTime(milliseconds: Long, block: () -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelScope.launch(Dispatchers.Main) {
                block()
            }
        }, milliseconds)
    }
}