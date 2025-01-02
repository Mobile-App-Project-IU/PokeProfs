package com.example.pokemonproject.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// Function to check if the device is connected to the internet
fun isInternetAvailable(context: Context): Boolean {
    // Get the system's ConnectivityManager service
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Get the active network (if there is one)
    val activeNetwork = connectivityManager.activeNetwork ?: return false

    // Get the network capabilities for the active network
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    // Return whether the network has internet capability
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
