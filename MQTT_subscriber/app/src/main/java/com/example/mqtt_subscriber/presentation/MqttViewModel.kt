package com.example.mqtt_subscriber.presentation

import android.util.Log
import androidx.lifecycle.*
import com.example.mqtt_subscriber.domain.usecase.AddCo2UseCase
import com.example.mqtt_subscriber.domain.usecase.GetAllCo2UseCase
import com.example.mqtt_subscriber.model.UiState
import com.example.mqtt_subscriber.model.UiState.*
import com.example.mqtt_subscriber.model.entity.Co2
import com.example.mqtt_subscriber.utils.constant
import com.example.mqtt_subscriber.utils.constant.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MqttViewModel
    @Inject constructor(
        private val addCo2UseCase: AddCo2UseCase,
        private val getAllCo2UseCase: GetAllCo2UseCase
): ViewModel(){

    private var _allDataUiState: MutableLiveData<UiState<List<Co2>>> = MutableLiveData()
    val allDataUiState: LiveData<UiState<List<Co2>>> = _allDataUiState

    private var _uiState: MutableLiveData<UiState<Co2>> = MutableLiveData()
    val uiState: LiveData<UiState<Co2>> = _uiState

    private var _connectUiState: MutableLiveData<UiState<String>> = MutableLiveData()
    val connectUiState: LiveData<UiState<String>> = _connectUiState


    fun connect(mqttAndroidClient : MqttAndroidClient, clientId: String) = viewModelScope.launch {
        _connectUiState.value = Loading()
        mqttAndroidClient.connect().actionCallback = object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken) {
                Log.d(TAG, "connected to ${constant.serverUri} with id $clientId")
                setSubscrition(mqttAndroidClient)
                setCallback(mqttAndroidClient)
                _connectUiState.value = Success("connected")
            }
            override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                Log.d(TAG, "error on connection:  $exception ")
                _connectUiState.value = Error("error on connection caused by: $exception ")
            }
        }
    }
    fun setCallback(mqttAndroidClient : MqttAndroidClient) {
        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.d(TAG,"connection lost caused by $cause")
                _uiState.value = Error("connection lost caused by $cause")
            }
            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                addCo2(String(message.payload))
                Log.d(TAG,"got: ${String(message.payload)}")
            }
            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Log.d(TAG,"delivery complete")
            }
        })
    }

    fun setSubscrition(mqttAndroidClient : MqttAndroidClient) {
        try {
            mqttAndroidClient.subscribe(constant.mqttTopic, 1)
            Log.d(TAG, "subscribed to topic: ${constant.mqttTopic}")
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun unSubscribe(topic: String, mqttAndroidClient: MqttAndroidClient) {
        try {
            mqttAndroidClient.unsubscribe(topic).actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    _connectUiState.value= Success("disconnected")
                    Log.d(TAG,"unsubscribed from topic $topic")
                }
                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.d(TAG,"Failed to unsubscribe caused by: $exception ")
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(mqttAndroidClient : MqttAndroidClient) = viewModelScope.launch{
        _connectUiState.value = Loading()
        unSubscribe(constant.mqttTopic,mqttAndroidClient)
//        mqttAndroidClient.unregisterResources()
        mqttAndroidClient.close()
        try {
            mqttAndroidClient.disconnect().actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    _connectUiState.value= Success("disconnected")
                    Log.d(TAG,"disconnected")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    _connectUiState.value = Error("Failed to disconnect caused by: $exception ")
                    Log.d(TAG,"Failed to disconnect caused by: $exception ")}
            }
        } catch (e: MqttException) {

            Log.e(TAG, "failed with exception: $e")
            e.printStackTrace()
        }
        mqttAndroidClient.setCallback(null)
    }

    fun addCo2(value: String) = viewModelScope.launch {
        val co2 = Co2(0,value = value, createAt = Date().time, state = "medium")
        addCo2UseCase.execute(co2)
        _uiState.value = Success(co2)
    }

    fun getCo2() = viewModelScope.launch {
        getAllCo2UseCase.execute().collect{
            _allDataUiState.value = Success(it)
        }
    }
}