package com.example.chatapplication.prefernces

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveName(name: String?) {
        prefs.edit().putString("name", name).apply()
    }

    fun saveBio(bio: String?) {
        prefs.edit().putString("bio", bio).apply()
    }

    fun saveImage(image: String?) {
        prefs.edit().putString("image", image).apply()
    }

    fun getName(): String? = prefs.getString("name", null)
    fun getBio(): String? = prefs.getString("bio", null)
    fun getImage(): String? = prefs.getString("image", null)

    fun clear() {
        prefs.edit().clear().apply()
    }
}