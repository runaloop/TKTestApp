package com.catp.tinkoffandroidlab.ui.main.data

import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("{path}/{page}?json=true")
    suspend fun fetch(@Path("path")path: String, @Path("page") page:Int): PageResponse
}