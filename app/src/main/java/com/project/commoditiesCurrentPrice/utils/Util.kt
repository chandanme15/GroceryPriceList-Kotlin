package com.project.commoditiesCurrentPrice.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.project.commoditiesCurrentPrice.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object {
        fun isNetworkAvailable(context : Context) : Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val mCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                mCapabilities != null &&
                        (mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                mCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            }
            else {
                val netInfo = connectivityManager.activeNetworkInfo
                netInfo != null && netInfo.isConnected
            }
        }

        fun showSnack(view : View, isError : Boolean, message: String) {
            val color = if (isError) Color.RED else Color.WHITE
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            val textView = snackbar.view.findViewById(R.id.snackbar_text) as TextView
            textView.setTextColor(color)
            snackbar.show()
        }

        fun formatDate(year : Int, month : Int, day : Int) : String {
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.DAY_OF_MONTH, day)
            cal.set(Calendar.MONTH, month-1)
            return DateFormat.getDateInstance().format(cal.time)
        }

        fun getDefaultDate() : String {
            return DateFormat.getDateInstance().format(Date(System.currentTimeMillis()-24*60*60*1000));
        }

        fun getCurrentDateAndTime() : String {
            return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date(System.currentTimeMillis()));
        }
    }
}