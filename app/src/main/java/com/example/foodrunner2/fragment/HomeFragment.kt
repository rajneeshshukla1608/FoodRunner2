package com.example.foodrunner2.fragment

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner2.R
import com.example.foodrunner2.adapter.HomeRecyclerAdapter
import com.example.foodrunner2.database.RestaurantDatabase
import com.example.foodrunner2.database.RestaurantEntity
import com.example.foodrunner2.model.Restaurant
import com.example.foodrunner2.util.ConnectionManager
import org.json.JSONException
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    lateinit var txtRestaurantName: TextView
    lateinit var imgRestaurantImage: ImageView
    lateinit var txtRestaurantPerson: TextView
    lateinit var txtRestaurantFavourite: TextView
    lateinit var txtRestaurantRating: TextView

    lateinit var recyclerhome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var btnCheckInternet: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    var bookId: String? = "100"

    val restaurantInfoList = arrayListOf<Restaurant>()

    lateinit var recyclerAdapter: HomeRecyclerAdapter
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerhome = view.findViewById(R.id.recyclerHome)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        txtRestaurantName = view.findViewById(R.id.txtRestaurantName)
        imgRestaurantImage = view.findViewById(R.id.imgRestaurantImage)
        txtRestaurantPerson = view.findViewById(R.id.txtRestaurantPrice)
        txtRestaurantFavourite = view.findViewById(R.id.txtRestaurantFavourite)
        txtRestaurantRating = view.findViewById(R.id.txtRestaurantRating)

        btnCheckInternet.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                //Internet is available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("success")
                dialog.setMessage("Internet connection found")
                dialog.setPositiveButton("ok") { text, listener ->
                    //Do nothing
                }
                dialog.setNegativeButton("cancel") { text, listener ->
                    //do nothing
                }
                dialog.create()
                dialog.show()
            } else {
                //Internet is not available
                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("error")
                dialog.setMessage("Internet connection not found")
                dialog.setPositiveButton("ok") { text, listener ->
                    //Do nothing
                }
                dialog.setNegativeButton("cancel") { text, listener ->
                    //do nothing
                }
                dialog.create()
                dialog.show()
            }
        }

        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                //here we will handle the response
//         println("the response is $it")

                val success = it.getBoolean("success")
                try {
                    progressLayout.visibility = View.GONE
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val restaurantJSONObject = data.getJSONObject(i)
                            val restaurantObject = Restaurant(
                                    restaurantJSONObject.getInt("id"),
                                    restaurantJSONObject.getString("name"),
                                    restaurantJSONObject.getString("rating"),
                                    restaurantJSONObject.getString("cost_for_one"),
                                    restaurantJSONObject.getString("image_url")
                            )
                            restaurantInfoList.add(restaurantObject)
                            recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantInfoList)


                            recyclerhome.adapter = recyclerAdapter

                            recyclerhome.layoutManager = layoutManager

                            recyclerhome.addItemDecoration(
                                    DividerItemDecoration(
                                            recyclerhome.context,
                                            (layoutManager as LinearLayoutManager).orientation
                                    )
                            )
                        }
                        val restaurantEntity = RestaurantEntity(
                                bookId?.toInt() as Int,
                                txtRestaurantName.text.toString(),
                                txtRestaurantPerson.text.toString(),
                                txtRestaurantRating.text.toString()
                        )





                    } else {
                        Toast.makeText(activity as Context, "Some error occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(activity as Context, "Unexpected Error Occurred!!!", Toast.LENGTH_SHORT).show()

                }


            }, Response.ErrorListener {
                //here we will handle errors
//             println("the error is $it")
                Toast.makeText(activity as Context, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()


            }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }

            }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
                //Do nothing
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
                //do nothing
            }
            dialog.create()
            dialog.show()
        }
        return view

    }

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int): AsyncTask<Void, Void, Boolean>(){

        /*
        Mode 1 -> Check Db if the restaurant is favourite or not
        Mode 2 -> Save the restaurant into DB as favourite
        Mode 3 -> Remove the restaurant from favourite
        * */

        val db = Room.databaseBuilder(context,RestaurantDatabase::class.java, "restaurants-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1 -> {
                    //Check Db if the restaurant is favourite or not

                    val restaurant: RestaurantEntity? = db.restaurantDao().getRestaurantById(restaurantEntity.restaurant_id.toString())
                    db.close()
                    return restaurant !=null
                }

                2 -> {
                    //Save the restaurant into DB as favourite
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true

                }

                3 -> {
                    //Remove the restaurant from favourite

                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true

                }
            }

            return false
        }

    }
}

//57dfec8c8e251b