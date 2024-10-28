package com.example.template.di

import com.example.template.BuildConfig
import com.example.template.model.network.AuthService
import com.example.template.model.network.ExampleService
import com.example.template.model.repository.AuthRepository
import com.example.template.model.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * @see ExampleService
 * @see AuthService
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(appInterceptor: AppInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(appInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideAppInterceptor(dataStoreRepository : DataStoreRepository, authRepositoryProvider: Provider<AuthRepository>): AppInterceptor {
        return AppInterceptor(dataStoreRepository, authRepositoryProvider)
    }

    class AppInterceptor @Inject constructor(
        private val dataStoreRepository: DataStoreRepository,
        private val authRepositoryProvider: Provider<AuthRepository>
    ) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = runBlocking { dataStoreRepository.getAccessToken().first() }

            val request = chain.request().newBuilder()
                .apply {
                    addHeader("Authorization", "Bearer $token")
                }
                .build()

            val response = chain.proceed(request)
            if (response.code == 401) {
                response.close()

                val refreshToken = runBlocking { dataStoreRepository.getRefreshToken().first() }
                val newAccessToken = runBlocking {
                    val tokenResponse = authRepositoryProvider.get().refreshAccessToken(refreshToken)
                    if (tokenResponse.isSuccess) {
                        tokenResponse.getOrNull()
                    } else {
                        null
                    }
                }

                if (newAccessToken != null) {
                    val newRequest = request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                    return chain.proceed(newRequest)
                }
            }

            return response
        }
    }

    @Provides
    @Singleton
    fun provideExampleService(retrofit: Retrofit): ExampleService {
        return retrofit.create(ExampleService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}
