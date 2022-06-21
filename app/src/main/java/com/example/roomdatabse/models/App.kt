package com.example.roomdatabse.models

import android.app.Application
import androidx.room.Room
import com.example.roomdatabse.db.CarDatabase

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        carDatabase = Room.databaseBuilder(
            this.applicationContext,
            CarDatabase::class.java,
            "car_database"
        ).allowMainThreadQueries().build()
    }
    companion object{
        lateinit var carDatabase: CarDatabase
    }
}