package com.example.template.model.repository

import android.util.Log
import com.example.template.model.network.ExampleService
import com.example.template.model.local.dao.ExampleDao
import com.example.template.model.entity.Example
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    private val exampleService: ExampleService,
    private val exampleDao: ExampleDao
){
    suspend fun getExampleData(): Example {
        return withContext(Dispatchers.IO) {
            try {
                val response = exampleService.getExampleData().execute()
                if (response.isSuccessful) {
                    response.body() ?: Example()
                } else {
                    Example()
                }

            } catch (e: Exception) {
                Example(accept = e.message)
            }
        }
    }
}