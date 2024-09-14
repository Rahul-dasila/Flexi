package com.example.flexie.di

import com.example.flexie.services.OneSignalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class OneSIgnalModule {
    @Provides
    @Singleton
    fun provideOneSingleRetrofit() : Retrofit {
        val retrofit = Retrofit.Builder().baseUrl("https://api.onesignal.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit):OneSignalService{
        return retrofit.create(OneSignalService::class.java)
    }
}