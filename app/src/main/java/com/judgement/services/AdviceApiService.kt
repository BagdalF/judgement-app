package com.example.projetoretrofit

import retrofit2.http.GET

interface AdviceApiService {
    @GET("advice")
    suspend fun getAdvice(): AdviceResponse
}