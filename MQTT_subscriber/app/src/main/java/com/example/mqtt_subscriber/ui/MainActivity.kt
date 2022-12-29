package com.example.mqtt_subscriber

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mqtt_subscriber.databinding.ActivityMainBinding
import com.example.mqtt_subscriber.utils.constant.TAG
import com.example.mqtt_subscriber.utils.constant.mqttTopic
import com.example.mqtt_subscriber.utils.constant.serverUri
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity() {
    private lateinit var mqttAndroidClient: MqttAndroidClient
    private val clientId: String = MqttClient.generateClientId()
    private var msg=""

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connect()
    }

    fun connect(){
            mqttAndroidClient = MqttAndroidClient(applicationContext,serverUri,clientId)
            mqttAndroidClient.connect().actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
//                    Log.d(TAG, "connected to $serverUri with id $clientId")
                    Toast.makeText(this@MainActivity, "connected to $serverUri with id $clientId",Toast.LENGTH_SHORT).show()
                    setSubscrition()
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
//                    Log.d(TAG, "error on connection:  $exception " )
                    Toast.makeText(this@MainActivity, "error on connection:  $exception ",Toast.LENGTH_SHORT).show()

                }
            }

            mqttAndroidClient.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable) {
                    Log.d(TAG,"connection lost caused by $cause")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    msg =  String(message.payload)
                    binding.txt.setText(msg)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    Log.d(TAG,"delivery complete")
                }
            })
        }

    fun setSubscrition() {
        try {
            mqttAndroidClient.subscribe(mqttTopic, 1)
            Log.d(TAG, "subscribed to topic: $mqttTopic")
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}