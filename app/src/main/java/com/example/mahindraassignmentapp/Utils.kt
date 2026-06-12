package com.example.mahindraassignmentapp

import android.content.Context
import android.content.Intent
import android.net.Uri

class Utils {

    fun goToMap(context: Context) {
        val latitude = 12.9716
        val longitude = 77.5946
        val label = Constants.CUSTOM_LOCATION

        val mapUri = Uri.parse("geo:0,0?q=$latitude,$longitude($label)")
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/?q=$latitude,$longitude")
            )
            context.startActivity(browserIntent)
        }
    }

}