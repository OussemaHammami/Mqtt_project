package com.example.mqtt_subscriber.domain

import androidx.lifecycle.LiveData
import com.example.mqtt_subscriber.model.entity.Co2
import kotlinx.coroutines.flow.Flow

interface MqttRepository {
    suspend fun insert(co2: Co2)

    suspend fun getAllCo2(): Flow<List<Co2>>

    suspend fun delete(co2: Co2)
}