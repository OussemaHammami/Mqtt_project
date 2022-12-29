package com.example.mqtt_subscriber.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "co2_table")
data class Co2(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val value: String,
    val state: String,
    val createAt: Long
)
