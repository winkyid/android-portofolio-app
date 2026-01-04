package com.slimmy.portoapps.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.slimmy.portoapps.R

object CustomToast {
    fun show(context: Context, message: String, iconRes: Int = R.drawable.ic_info) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.layout_custom_toast, null)

        val text = layout.findViewById<TextView>(R.id.tv_toast_message)
        val icon = layout.findViewById<ImageView>(R.id.iv_toast_icon)

        text.text = message
        icon.setImageResource(iconRes)

        with(Toast(context)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}