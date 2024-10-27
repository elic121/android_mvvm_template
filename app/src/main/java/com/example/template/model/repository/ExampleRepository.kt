package com.example.template.model.repository

import com.example.template.model.network.ExampleService
import com.example.template.model.entity.ExampleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    private val exampleService: ExampleService,
//    private val exampleDao: ExampleDao
){
    suspend fun getExampleData(): Result<ExampleEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = exampleService.getExampleData().execute()
                if (response.isSuccessful) {
                    response.body()?.let { exampleEntity ->
                        Result.success(exampleEntity)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}