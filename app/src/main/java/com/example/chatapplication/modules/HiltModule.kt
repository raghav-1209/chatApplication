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
import com.example.chatapplication.apis.RefreshApi
import com.example.chatapplication.apis.TokenAuthenticator
import com.example.chatapplication.auth.AuthManager
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
import java.util.concurrent.TimeUnit
import javax.inject.Named

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
    fun provideHttpClient(): HttpClient {
        return HttpClient()
    }

    @Provides
    @Singleton
    @Named("refresh_client")
    fun provideRefreshOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    @Named("refresh")
    fun provideRefreshRetrofit(
        @Named("refresh_client") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UrlConstants.emUrl + "/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        sessionManager: SessionManager,
        @Named("refresh_api") refreshApi: RefreshApi,
        authManager: AuthManager
    ): TokenAuthenticator {
        return TokenAuthenticator(
            sessionManager = sessionManager,
            apis = refreshApi,
            authManager = authManager
        )
    }

    @Provides
    @Singleton
    @Named("refresh_api")
    fun provideRefreshApi(
        @Named("refresh") retrofit: Retrofit
    ): RefreshApi {
        return retrofit.create(RefreshApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
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
    fun provideWebSocketManager(
        client: HttpClient,
        fbAuth: FirebaseAuth,
        apis: DataBaseApis,
        sessionManager: SessionManager
    ): WebSocketManager {
        return WebSocketManager(client, apis, fbAuth, sessionManager)
    }
    @Provides
    @Singleton
    fun provideConnectionController(webSocketManager: WebSocketManager)= ConnectionController(webSocketManager)


}