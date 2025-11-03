package com.example.projetoretrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

class AdviceViewModel : ViewModel() {
    val advice = mutableStateOf("Fetching advice...")

    init {
        fetchAdvice()
    }

    fun fetchAdvice() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getAdvice()

                //And Remove the advice with _advice
                advice.value = response.slip.advice
            } catch (e: Exception) {
                advice.value = "Error: ${e.message}"
            }
        }
    }
}