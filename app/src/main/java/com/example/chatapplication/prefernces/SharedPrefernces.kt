package com.example.chatapplication.prefernces

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferences @Inject constructor(val context: Context) {
    private val myPref = context.getSharedPreferences("my_pref", Context.MODE_PRIVATE)

    fun saveRefreshToken(uid: String, token: String) {
        myPref.edit().putString("${uid}_refreshToken", token).apply()
    }

    fun saveAccessToken(uid: String, token: String) {
        myPref.edit().putString("${uid}_accessToken", token).apply()
    }

    fun getRefreshToken(uid: String): String? {
        return myPref.getString("${uid}_refreshToken", null)
    }

    fun getAccessToken(uid: String): String? {
        return myPref.getString("${uid}_accessToken", null)
    }

    fun clearTokens(uid: String) {
        myPref.edit().remove("${uid}_refreshToken").remove("${uid}_accessToken").apply()
    }
  private   val prefs = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
    fun saveBio(uid: String, bio: String?) {

        prefs.edit().putString("BIO_$uid", bio).apply()
    }

    fun getBio(uid: String): String? {
        return prefs.getString("BIO_$uid", null)
    }
    fun saveName(uid: String,name: String){
        prefs.edit().putString("user_name${uid}",name).apply()
    }
    fun getName(uid: String): String?{
        return prefs.getString("user_name${uid}",null)
    }
    fun saveImage(uid:String,img_Url: String?){
        prefs.edit().putString("image_url",img_Url).apply()
    }
    fun getImage(uid: String): String?{
        return prefs.getString("image_url",null)
    }

}
