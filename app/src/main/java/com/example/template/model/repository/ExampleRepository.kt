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
    suspend fun getExampleData(): ExampleEntity {
        return withContext(Dispatchers.IO) {
            try {
                val response = exampleService.getExampleData().execute()
                if (response.isSuccessful) {
                    response.body() ?: ExampleEntity()
                } else {
                    ExampleEntity()
                }

            } catch (e: Exception) {
                ExampleEntity(host=e.message)
            }
        }
    }
}