package com.example.roomdatabse

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import com.example.roomdatabse.databinding.ActivityMainBinding
import com.example.roomdatabse.adapter.CarAdapter
import com.example.roomdatabse.adapter.ItemOnClickListenner
import com.example.roomdatabse.databinding.DialogBinding
import com.example.roomdatabse.db.CarDao
import com.example.roomdatabse.models.App
import com.example.roomdatabse.models.Car


class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener, ItemOnClickListenner {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter: CarAdapter by lazy {
        CarAdapter(actionListener = this)
    }
    private val dao: CarDao by lazy {
        App.carDatabase.carDao()
    }
    var allCarList = mutableListOf<Car>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainRv.adapter = adapter
        binding.searchView.setOnQueryTextListener(this)
        val dao = App.carDatabase.carDao()

        getAllCars()

        binding.addCar.setOnClickListener {
            addNewCar(dao)
        }
    }


    private fun addNewCar(dao: CarDao) = binding.apply {
        val title = carNameText.text.toString()
        val price = carPriceText.text.toString()
        val newCar = Car(title = title, price = price)
        dao.addNewCar(car = newCar)
        adapter.addNewCar(car = newCar)
    }

    private fun getAllCars() {
        allCarList = dao.getAllCars()
        adapter.carList = allCarList
    }

    override fun showCustomAlertDialog(position: Int, car: Car) {
        val dialogBinding = DialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            val dialog = AlertDialog.Builder(this@MainActivity)
                .setTitle("Удалить или обновить")
                .setView(root)
                .setPositiveButton("Обновить", null)
                .setNegativeButton("Удалить", null)
                .setNeutralButton("Отменить", null)
                .setPositiveButton("Добавить",null)
                .create()
            dialog.setOnShowListener {
                carModel.setText(car.title)
                carPrice.setText(car.price)

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val newName = carModel.text.toString()
                    val newPrice = carPrice.text.toString()
                    val newCar = Car(id = car.id, title = newName, price = newPrice)
                    dao.updateCar(newCar)
                    adapter.updateCar(position = position, newCar = newCar)
                    dialog.dismiss()
                }
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    dao.deleteCar(car = car)
                    adapter.deleteCar(position = position)
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    override fun onQueryTextSubmit(newText: String?): Boolean {
        if (newText != null) {
            searchCars(newText = newText)
        }else adapter.carList = allCarList
        return false
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!=null) {
            searchCars(newText = newText)
        }else{
            adapter.carList = allCarList
        }
        return false
    }

    private fun searchCars(newText: String) {
        getAllCars()
        val searchText = newText.lowercase()
        val searchCars = mutableListOf<Car>()
        allCarList.forEach { car ->
            val name = car.price.lowercase()
            if (name.contains(searchText)) {
                searchCars.add(car)
            }

        }
        adapter.carList = searchCars
    }
}