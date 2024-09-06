package com.learning.mygenai.di

import com.learning.mygenai.ApplicationDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class DispatchersModule {

    @Provides
    fun providesDispatcher():ApplicationDispatchers {
        return ApplicationDispatchers(ioDispatcher=Dispatchers.IO, defaultDispatcher = Dispatchers.Default, mainDispatcher = Dispatchers.Main)
    }

}