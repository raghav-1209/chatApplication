package com.example.chatapplication.modules

import android.content.Context
import com.example.chatapplication.ConnectionController
import com.google.android.gms.common.data.DataBufferRef
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton
import com.example.chatapplication.apis.DataBaseApis
import com.example.chatapplication.client.MyHttpClient
import com.example.chatapplication.client.WebSocketManager
import com.example.chatapplication.constants.UrlConstants
import com.example.chatapplication.prefernces.SharedPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import okhttp3.OkHttpClient


@Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context)=context
    @Provides
    @Singleton
    fun providePref(context: Context)= SharedPreferences(context)



    @Provides
        @Singleton
        fun provideRetrofit(): Retrofit =
            Retrofit.Builder()
                .baseUrl(UrlConstants.emUrl+ "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        @Singleton
        @Provides
        fun provideApi(retrofit: Retrofit): DataBaseApis =retrofit.create(DataBaseApis::class.java)
  @Provides
  @Singleton
  fun provideDatBaseRep(api: DataBaseApis,sharedPreferences: SharedPreferences)= DataBaseRep(api, sharedPreferences)
    @Provides
    @Singleton
    fun provideFireBaseAuth()= FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = MyHttpClient.myClient
    @Provides
    @Singleton
    fun provideWebSocketManager(client: HttpClient,fbAuth: FirebaseAuth,apis: DataBaseApis,sharedPreferences: SharedPreferences)= WebSocketManager(client,apis,fbAuth,sharedPreferences)
    @Provides
    @Singleton
    fun provideConnectionController(webSocketManager: WebSocketManager)= ConnectionController(webSocketManager)


}