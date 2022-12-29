package com.example.mqtt_subscriber.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mqtt_subscriber.databinding.ActivityMainBinding
import com.example.mqtt_subscriber.model.UiState.*
import com.example.mqtt_subscriber.presentation.MqttViewModel
import com.example.mqtt_subscriber.utils.constant.serverUri
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mqttAndroidClient: MqttAndroidClient
    private lateinit var clientId: String
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MqttViewModel by viewModels()
    private var arrayList = ArrayList<Entry>()
    private val currentTimeMillis = System.currentTimeMillis()

    private var connect = true /* counter for btn connect/disconnect */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val desc = Description()
        desc.text = "Real time co2 data"
        binding.graph.description =desc
        binding.graph.visibility = View.GONE
        binding.graph.setDrawGridBackground(false)
        binding.graph.xAxis.setDrawGridLines(false)
        binding.graph.axisLeft.setDrawGridLines(false)
        binding.graph.axisRight.setDrawGridLines(false)
//        binding.graph.setTouchEnabled(false)
        binding.graph.enableScroll()
        binding.graph.isDragEnabled = true
        binding.graph.axisRight.axisMinimum = 200F
        binding.graph.axisLeft.axisMinimum = 200F


        clientId = MqttClient.generateClientId()
        mqttAndroidClient = MqttAndroidClient(applicationContext,serverUri,clientId)
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        viewModel.connectUiState.observe(this, Observer{
            when (it) {
                is Loading -> showLoading()
                is Success -> {
                    stopLoading()
                    if (it.data == "connected"){
                        Toast.makeText(this@MainActivity, "connected to $serverUri with id $clientId",Toast.LENGTH_SHORT).show()
                        binding.btnConnect.text = "disconnect"
                    }
                    else if (it.data == "disconnected"){
                        Toast.makeText(this@MainActivity, "disconnected from $serverUri",Toast.LENGTH_SHORT).show()
                        binding.btnConnect.text = "connect"
                    }
                }
                is Error -> {
                    stopLoading()
                    processError(it.msg)
                }
            }
        })

        binding.btnConnect.setOnClickListener {
            if(connect){
                viewModel.connect(mqttAndroidClient,clientId)
                connect = false
            }
            else{
                viewModel.disconnect(mqttAndroidClient)
                connect = true
            }
        }

        viewModel.uiState.observe(this, Observer{
            when (it) {
                is Loading -> {}
                is Success -> {
                    binding.txt.text = it.data?.value
                    binding.graph.visibility = View.VISIBLE
/*
                    add entry to graph
*/
                    arrayList.add(Entry((System.currentTimeMillis() - currentTimeMillis)/1000.toFloat(), it.data!!.value.toFloat()))
                    binding.graph.data = LineData(LineDataSet(arrayList, "co2"))
                    binding.graph.data.notifyDataChanged()
                    binding.graph.setVisibleXRangeMaximum(10F)
                    binding.graph.moveViewToX((System.currentTimeMillis() - currentTimeMillis)/1000.toFloat())
                }
                is Error -> processError(it.msg)
            }
        })
    }

    override fun onDestroy() {
        arrayList.clear()
        super.onDestroy()
    }

    private fun processError(msg: String?) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.prbar.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding.prbar.visibility = View.GONE
    }
}