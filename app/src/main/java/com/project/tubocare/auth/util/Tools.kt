package com.project.tubocare.auth.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date

class Tools {
    companion object {
        fun openLink(mContent: Context, url: String){
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(url)
            mContent.startActivity(openUrl)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val formatDate = SimpleDateFormat("dd/MM/yyyy")
            return formatDate.format(date)
        }
    }
}