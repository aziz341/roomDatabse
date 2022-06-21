package com.example.roomdatabse.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomdatabse.models.Car

@Database(entities = [Car::class], version = 1)
abstract class CarDatabase:RoomDatabase() {

    abstract fun carDao(): CarDao

}