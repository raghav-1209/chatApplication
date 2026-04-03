package com.example.chatapplication.apis

import com.example.chatapplication.models.BioData
import com.example.chatapplication.models.ChatData
import com.example.chatapplication.models.FcmData
import com.example.chatapplication.models.FollowInfo
import com.example.chatapplication.models.FollowState
import com.example.chatapplication.models.SignInData
import com.example.chatapplication.models.StatusModel
import com.example.chatapplication.models.StatusWithUser
import com.example.chatapplication.models.UserSession
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.models.isOnline
import com.example.chatapplication.models.loginData
import com.example.chatapplication.repository.MessageData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DataBaseApis {
    @POST("/auth/signIn")
    suspend fun signIn(@Body body: SignInData): UserSession
    @POST("/auth/login")
    suspend fun login(@Body body: loginData): UserSession
    @POST("/auth/fcmToken")
    suspend fun fcmTokenSave(@Body fcmData: FcmData)
    @GET("/profile/userInfo")
    suspend fun getUserData(@Header("Authorization") token: String): WholeUser
         @POST("/profile/saveBio")
         suspend fun saveBio(
             @Header("Authorization") token: String,
             @Body bioData: BioData): BioData
         @GET("/profile/getBio")
         suspend fun getBio(@Header("Authorization") token: String): BioData
         @GET("/check")
         suspend fun checking(@Header("Authorization") token: String): Info
         @POST("/auth/refreshToken")
         suspend fun refreshToken(@Body token: Info): UserSession
         @GET("/profile/allUsers")
         suspend fun  getAllUsers(@Header("Authorization") token: String):List<WholeUser>
         @POST("/activity/follow/{following_uid}")
         suspend fun followUser(
             @Header("Authorization") token: String,
             @Path("following_uid") followingUid: String): FollowState
         @POST("/activity/accept/{follower_Uid}")
         suspend fun acceptReq(@Header("Authorization") token: String,
                               @Path("follower_Uid") uid: String

         )
         @POST("/activity/reject/{follower_Uid}")
         suspend fun rejectReq(@Header("Authorization") token: String,
                               @Path("follower_Uid") uid: String)
         @GET("/activity/followState/{following_Uid}")
         suspend fun getFollowState(@Header("Authorization") token: String,@Path("following_Uid") uid: String): FollowState
         @GET("/activity/getFollowers")
         suspend fun getFollowers(
             @Header("Authorization") token: String,
                                  ): FollowInfo
         @GET("/activity/getFollowing")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
    ): FollowInfo
    @GET("/profile/getUser/{following_Uid}")
    suspend fun getUserByUid(@Header("Authorization")token:String,@Path("following_Uid")uid: String): WholeUser
  @GET("/isOnline/{following_Uid}")
  suspend fun isOnline(@Header("Authorization")token: String,@Path("following_Uid")following_Uid: String): isOnline
  @GET("/chats/getMessages/{chat_id}")
  suspend fun getMessages(@Header("Authorization") token: String,@Path("chat_id")chat_id: String):List<ChatData>
  @POST("/chats/aiChat")
  suspend fun sendMessage(@Header("Authorization")token: String,@Body message: MessageData): MessageData
  @GET("/status/getStatus")
  suspend fun getStatusImage(@Header("Authorization")token: String):List<StatusModel>
    @GET("/status/getFollowingStatus")
    suspend fun getFollowersStatus(@Header("Authorization")token: String):List<StatusWithUser>
    @GET("/status/deleteStatus")
    suspend fun deleteStatus(@Header("Authorization") token: String):response
    @DELETE("/chats/deleteChat/{chat_id}")
    suspend fun ClearChat(@Header("Authorization") token: String,@Path("chat_id") chat_id: String):response
    @DELETE("/chats/deleteMessage/{message_id}")
    suspend fun  deleteMessage(@Header("Authorization") token: String,@Path("message_id") message_Id: String):response
}

data class Info(
    val token: String
)
data class response(
    val success: Boolean,
    val text: String
)