package com.example.mqtt_subscriber.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mqtt_subscriber.model.dao.Co2Dao
import com.example.mqtt_subscriber.model.entity.Co2

@Database(
    entities = [Co2::class],
    version = 1
)
abstract class Co2DB : RoomDatabase() {
    abstract fun dao() : Co2Dao
}