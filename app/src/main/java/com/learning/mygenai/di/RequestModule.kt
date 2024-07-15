package com.learning.mygenai.di

import android.content.Context
import com.learning.mygenai.BASE_URL
import com.learning.mygenai.network.ChatAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RequestModule {

    @Singleton
    @Provides
    fun providesRetrofitObject(@ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesChatAPIService(retrofit: Retrofit):ChatAPIService {
        return retrofit.create(ChatAPIService::class.java)
    }

}