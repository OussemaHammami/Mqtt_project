package com.example.mqtt_subscriber.di

import com.example.mqtt_subscriber.domain.MqttRepository
import com.example.mqtt_subscriber.domain.usecase.AddCo2UseCase
import com.example.mqtt_subscriber.domain.usecase.GetAllCo2UseCase
import com.example.mqtt_subscriber.model.MqttRepositoryImpl
import com.example.mqtt_subscriber.model.db.Co2DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent :: class, ViewModelComponent::class)
class DomainModule {
    @Provides
    fun providesCo2Repository(db: Co2DB): MqttRepository = MqttRepositoryImpl(db)

    @Provides
    fun providesGetAllCo2UseCase(repository: MqttRepository) = GetAllCo2UseCase(repository)

    @Provides
    fun providesAddCo2UseCase(repository: MqttRepository) = AddCo2UseCase(repository)
}