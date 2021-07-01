package com.example.foodrunner2.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class], version = 1 ) //version always will be a natural number
abstract class RestaurantDatabase: RoomDatabase() {

     abstract fun restaurantDao(): RestaurantDao


}


//there is no any default implementation for the dao interface