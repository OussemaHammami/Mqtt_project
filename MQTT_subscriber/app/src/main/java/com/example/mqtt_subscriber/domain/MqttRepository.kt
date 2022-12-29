package com.example.mqtt_subscriber.model.repository

import android.util.Log
import android.widget.Toast
import com.example.mqtt_subscriber.utils.constant
import com.example.mqtt_subscriber.utils.constant.TAG
import com.example.mqtt_subscriber.utils.constant.serverUri
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

interface MqttRepository {
    fun connect(mqttAndroidClient : MqttAndroidClient, clientId: String)

    fun setSubscrition(mqttAndroidClient : MqttAndroidClient)
}