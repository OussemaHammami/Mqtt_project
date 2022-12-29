package com.example.mqtt_subscriber.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mqtt_subscriber.model.entity.Co2
import kotlinx.coroutines.flow.Flow

@Dao
interface Co2Dao {
    @Insert
    suspend fun addCo2(co2: Co2)

    @Query("SELECT * FROM co2_table")
    fun getAllCo2(): Flow<List<Co2>>

    @Query("SELECT * FROM co2_table ORDER BY createAt LIMIT 1")
    fun getLastCo2(): LiveData<Co2>

    @Delete
    suspend fun deleteCo2(co2: Co2)

}