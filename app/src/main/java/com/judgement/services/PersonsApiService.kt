package com.judgement.services

import com.judgement.data.local.APIPersons
import retrofit2.http.GET

interface PersonsApiService {
    @GET("users")
    suspend fun getPersons(): List<APIPersons>
}