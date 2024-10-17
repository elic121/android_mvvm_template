package com.example.template.model.network

import com.example.template.model.entity.Example
import retrofit2.Call
import retrofit2.http.GET

interface ExampleService {

    @GET("/")
    fun getExampleData(): Call<Example>
}