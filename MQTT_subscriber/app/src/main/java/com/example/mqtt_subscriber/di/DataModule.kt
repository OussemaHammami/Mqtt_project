package com.example.mqtt_subscriber.di

import android.content.Context
import androidx.room.Room
import com.example.mqtt_subscriber.model.db.Co2DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): Co2DB = Room
        .databaseBuilder(appContext, Co2DB::class.java, "co2_db")
        .build()

    @Singleton
    @Provides
    fun providesCo2Dao(db: Co2DB) = db.dao()
    
}