package com.example.foodrunner2.database

import androidx.annotation.RequiresPermission
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {

    @Insert
    fun insertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM restaurants")
    fun getAllRestaurant(): List<RestaurantEntity>

    @Query("SELECT * FROM restaurants WHERE restaurant_id = :restaurantId") //we are using : before restaurantId because we tell the compiler that the value will come from this function written just below
    fun getRestaurantById(restaurantId: String): RestaurantEntity

}
