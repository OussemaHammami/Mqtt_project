package com.example.mqtt_subscriber.domain.usecase

import com.example.mqtt_subscriber.domain.MqttRepository

class GetAllCo2UseCase(
    private val repository: MqttRepository
) {
    suspend fun execute() = repository.getAllCo2()
}