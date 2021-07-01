package com.example.foodrunner2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunner2.R
import com.example.foodrunner2.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context,val itemList: ArrayList<Restaurant>): RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row, parent, false)

        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val Restaurant = itemList[position]
        holder.txtRestaurantName.text = Restaurant.restaurantName
        holder.txtRestaurantPerPerson.text = Restaurant.restaurantCostPerPerson
        holder.txtRestaurantRating.text = Restaurant.restaurantRating
        Picasso.get().load(Restaurant.restaurantImage_url).error(R.drawable.default_book_cover).into(holder.imgRestaurantImage)
//        holder.imgRestaurantImage.setImageResource(Restaurant.restaurantImage_url)

                   //there is also an method called the setBackgroundResource this will
                 // not so the image properly the image will be in the background not in the front
                 // and the default image will be in the background part

       holder.llcontent.setOnClickListener {
           Toast.makeText(context,"Clicked on ${holder.txtRestaurantName}", Toast.LENGTH_SHORT).show()
       }

    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtRestaurantPerPerson: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val llcontent: LinearLayout = view.findViewById(R.id.llcontent)
    }
}