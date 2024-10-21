package com.example.template.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

fun Context.toast(message: Any) {
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * Example usage:
 *
 * val bundle = Bundle().apply {
 *     putString("key", "value")
 *     putInt("age", 25)
 * }
 *
 * goToActivity(AnotherActivity::class.java, bundle)
 *
 * This will navigate to AnotherActivity and pass the provided data.
 */
fun Context.goToActivity(activity: Class<*>, extras: Bundle? = null, clearStack: Boolean = false) {
    try {
        val intent = Intent(this, activity).apply {
            extras?.let { putExtras(it) }
            if (clearStack) {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        }
        startActivity(intent)
    } catch (e: Exception) {
        this.toast(e.toString())
        e.printStackTrace()
    }
}
