package com.example.template

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.template.di.NetworkModule
import com.example.template.model.entity.ExampleEntity
import com.example.template.model.network.ExampleService
import com.example.template.model.repository.ExampleRepository
import com.example.template.viewmodel.ExampleViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Example API test
 * @see NetworkModule
 * @see ExampleService
 * @see ExampleRepository
 * @see ExampleViewModel
 */
@RunWith(AndroidJUnit4::class)
class ExampleEntityNetworkTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ExampleService
    private lateinit var repository: ExampleRepository
    private lateinit var viewModel: ExampleViewModel

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(NetworkModule.AppInterceptor(context))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ExampleService::class.java)
        repository = ExampleRepository(service)
        viewModel = ExampleViewModel(repository)
    }

    @Test
    fun testNetworkError() = runBlocking {
        val errorRetrofit = Retrofit.Builder()
            .baseUrl("https://invalid-url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val errorService = errorRetrofit.create(ExampleService::class.java)

        try {
            errorService.getExampleData().execute()
            fail("Should throw an exception")
        } catch (e: Exception) {
            assertTrue(true)
            println("Expected error occurred: ${e.message}")
        }
    }

    @Test
    fun testNetworkCallThroughViewModel() {
        val latch = CountDownLatch(1)
        var result: ExampleEntity? = null

        viewModel.exampleEntity.observeForever { example ->
            result = example
            latch.countDown()
        }

        viewModel.getExampleData()

        assertTrue("Network call timed out", latch.await(5, TimeUnit.SECONDS))
        assertNotNull("Response should not be null", result)

        println("Response: $result")
    }

    @Test
    fun testDirectApiCall() = runBlocking {
        val response = service.getExampleData().execute()

        assertTrue("API call should be successful", response.isSuccessful)
        assertNotNull("Response body should not be null", response.body())

        val example = response.body()

        assertNotNull("X-Cloud-Trace-Context field should not be null", example?.xCloudTraceContext)
        assertNotNull("traceParent field should not be null", example?.traceparent)
        assertNotNull("User-Agent field should not be null", example?.userAgent)
        assertNotNull("Host field should not be null", example?.host)

        // error case
        // assertTrue("Upgrade-Insecure-Requests field should not be empty or null", !example?.upgradeInsecureRequests.isNullOrEmpty())

        println("API Response: ${response.body()}")
    }
}
