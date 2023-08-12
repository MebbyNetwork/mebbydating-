package com.example.mebby.di

import com.example.data.annotation.CityRetrofitClient
import com.example.data.interfaces.GeoDBApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @CityRetrofitClient
    fun httpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", "c63726ea0cmsh2f3e36c36d4fe18p1e449bjsn6c61baf6f73a")
                    .addHeader("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                    .build()

                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @CityRetrofitClient
    fun cityRetrofit(@CityRetrofitClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://wft-geo-db.p.rapidapi.com/v1/geo/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCityApi(@CityRetrofitClient retrofit: Retrofit): GeoDBApi {
        return retrofit.create(GeoDBApi::class.java)
    }
}