package com.example.chatapplication.modules

import android.content.Context
import com.example.chatapplication.ConnectionController
import com.example.chatapplication.apis.AuthInterceptor
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
import com.example.chatapplication.auth.AuthManager
import com.example.chatapplication.client.MyHttpClient
import com.example.chatapplication.client.WebSocketManager
import com.example.chatapplication.constants.UrlConstants
import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context)=context
    @Provides
    @Singleton
    fun sessionManager(context: Context)= SessionManager(context)
    @Provides
    @Singleton
    fun userPreferences(context: Context)= UserPreferences(context)
    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): AuthInterceptor {
        return AuthInterceptor(sessionManager)
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(UrlConstants.emUrl+ "/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        @Singleton
        @Provides
        fun provideApi(retrofit: Retrofit): DataBaseApis =retrofit.create(DataBaseApis::class.java)
    @Provides
    @Singleton
    fun provideAuthManager(sessionManager: SessionManager): AuthManager= AuthManager(sessionManager =sessionManager )
  @Provides
  @Singleton
  fun provideDatBaseRep(api: DataBaseApis,sessionManager: SessionManager,authManager: AuthManager)= DataBaseRep(api, sessionManager,authManager)
    @Provides
    @Singleton
    fun provideFireBaseAuth()= FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = MyHttpClient.myClient
    @Provides
    @Singleton
    fun provideWebSocketManager(client: HttpClient,fbAuth: FirebaseAuth,apis: DataBaseApis,sessionManager: SessionManager)= WebSocketManager(client,apis,fbAuth,sessionManager)
    @Provides
    @Singleton
    fun provideConnectionController(webSocketManager: WebSocketManager)= ConnectionController(webSocketManager)


}