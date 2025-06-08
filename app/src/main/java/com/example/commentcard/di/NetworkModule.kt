package com.example.commentcard.di

import com.example.commentcard.data.remote.APIService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * A hilt module to provide network related dependencies.
 * This ensures that components like Retrofit and ApiService are created once and shared across the application (as singletons).
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    /**
     * Provides a singleton Moshi instance for JSON serialization and deserialization.
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Provides singleton retrofit instance.
     * @param moshi The Moshi instance for JSON conversion
     */
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    /**
     * Provides singleton instance of APIService.
     * @param retrofit The Retrofit instance to create the service
     */
    fun provideAPIService(retrofit: Retrofit): APIService = retrofit.create(APIService::class.java)
}