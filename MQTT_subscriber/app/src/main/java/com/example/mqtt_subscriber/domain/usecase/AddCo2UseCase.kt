package com.example.mqtt_subscriber.domain.usecase

import com.example.mqtt_subscriber.domain.MqttRepository
import com.example.mqtt_subscriber.model.entity.Co2

class AddCo2UseCase (
    private val repository: MqttRepository
    ){
    suspend fun execute(co2: Co2) = repository.insert(co2)
}