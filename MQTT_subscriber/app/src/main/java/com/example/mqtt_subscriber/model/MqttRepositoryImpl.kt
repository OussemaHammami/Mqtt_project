package com.example.mqtt_subscriber.model

import androidx.lifecycle.LiveData
import com.example.mqtt_subscriber.domain.MqttRepository
import com.example.mqtt_subscriber.model.db.Co2DB
import com.example.mqtt_subscriber.model.entity.Co2
import kotlinx.coroutines.flow.Flow

class MqttRepositoryImpl(
    private val db: Co2DB
) : MqttRepository {
    override suspend fun insert(co2: Co2) {
        return db.dao().addCo2(co2)
    }

    override suspend fun getAllCo2(): Flow<List<Co2>> {
        return db.dao().getAllCo2()
    }

    override suspend fun delete(co2: Co2) {
    }
}