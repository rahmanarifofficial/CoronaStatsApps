package com.rahmanarifofficial.coronastats.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller

object Utils {
    fun showToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun installGooglePlayServicesProvider(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //Devices with Android 5.1+ should support TLS 1.x out of the box
            try {
                ProviderInstaller.installIfNeeded(context)
            } catch (e: GooglePlayServicesRepairableException) {
                Log.e("ProviderInstaller", "Google Play Services is out of date!", e)
                GoogleApiAvailability.getInstance()
                    .showErrorNotification(context, e.connectionStatusCode)
            } catch (e: GooglePlayServicesNotAvailableException) {
                Log.e("ProviderInstaller", "Google Play Services is unavailable!", e)
            }
        }
    }
}