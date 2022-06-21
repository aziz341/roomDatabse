package com.example.roomdatabse.db

import androidx.room.*
import com.example.roomdatabse.models.Car
import kotlinx.coroutines.selects.select

@Dao
interface CarDao {

    @Query("select * from car_table" )
    fun getAllCars(): MutableList<Car>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewCar(car: Car)

    @Update
    fun updateCar(car: Car)

    @Query("select * from car_table where id ==:carid")
    fun getCarById(carid:Int): Car

    @Delete
    fun deleteCar(car: Car)
}