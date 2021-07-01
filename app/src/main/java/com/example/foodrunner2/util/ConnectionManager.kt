package com.example.foodrunner2.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {
    fun checkConnectivity(context: Context): Boolean{
             //first to check that what type of network it is connected now and
        //then check that there is a internet or not this whole is done by the connectivity manager\

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

        if (activeNetwork?.isConnected != null){
            return activeNetwork.isConnected
        } else {
            return false
        }
    }
}