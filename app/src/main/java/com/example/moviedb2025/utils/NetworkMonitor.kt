package com.example.moviedb2025.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

object NetworkMonitor {
    private var isRegistered = false
    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var onNetworkAvailable: () -> Unit
    private lateinit var onNetworkLost: () -> Unit

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("NetworkMonitor", "Network is available")
            onNetworkAvailable()
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val unmetered = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            Log.d("NetworkMonitor", "Network capabilities changed: unmetered = $unmetered")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("NetworkMonitor", "Network is lost")
            onNetworkLost()
        }
    }

    fun registerNetworkCallback(context: Context, onAvailable: () -> Unit, onLost: () -> Unit) {
        if (isRegistered) return
        isRegistered = true

        onNetworkAvailable = onAvailable
        onNetworkLost = onLost

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        // Also register to listen to ongoing connectivity changes
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregisterNetworkCallback() {
        if (!isRegistered) return
        isRegistered = false
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}