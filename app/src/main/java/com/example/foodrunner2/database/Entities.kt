package com.example.foodrunner2.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")    // Annotation to tell the compiler that what we are creating
data class RestaurantEntity (
        @PrimaryKey val restaurant_id: Int,
        @ColumnInfo(name = "restaurant_name") val restaurantName: String,
        @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
        @ColumnInfo(name = "restaurant_per_person") val restaurantCostPerPerson: String
//        @ColumnInfo(name = "restaurant_image")val restaurantImage_url: String
)