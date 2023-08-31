package com.example.todoproject.di

import com.example.todoproject.data.contract.FirebaseRealtimeInterface
import com.example.todoproject.data.datasource.FirebaseRealtimeDatasource
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// provides interaction with DB(authentication) for using in Fragment
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesDataSource(auth: FirebaseAuth): FirebaseRealtimeInterface {
        return FirebaseRealtimeDatasource(auth = auth)
    }

}