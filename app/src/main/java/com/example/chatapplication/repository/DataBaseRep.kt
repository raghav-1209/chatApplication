package com.example.chatapplication.repository

import android.util.Log
import com.example.chatapplication.apis.DataBaseApis
import com.example.chatapplication.apis.Info
import com.example.chatapplication.client.MyHttpClient
import com.example.chatapplication.constants.UrlConstants
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
import com.example.chatapplication.prefernces.SharedPreferences
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataBaseRep @Inject constructor( val apis: DataBaseApis,val sharedPreferences: SharedPreferences) {
    private suspend fun <T> safeApiCall(
        uid: String,
        apiCall: suspend (token: String) -> T
    ): Result<T> {

        return try {
            val token = sharedPreferences.getAccessToken(uid) ?: ""
            val response = apiCall("Bearer $token")
            Result.success(response)

        } catch (e: HttpException) {
            if (e.code() == 401) {
                try {
                    val refreshToken = sharedPreferences.getRefreshToken(uid) ?: ""
                    val newTokenResponse = apis.refreshToken(Info(refreshToken))

                    sharedPreferences.saveAccessToken(uid, newTokenResponse.token)
                    sharedPreferences.saveRefreshToken(uid, newTokenResponse.refreshToken)

                    val retry = apiCall("Bearer ${newTokenResponse.token}")

                    Result.success(retry)

                } catch (retryError: Exception) {
                    Log.e("DataBaseRep", "Retry failed ${retryError.message}")
                    Result.failure(retryError)
                }

            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveTokenInDb(fcmData: FcmData) {
        withContext(Dispatchers.IO) {
            try {
                apis.fcmTokenSave(fcmData)

            } catch (e: Exception) {
                Log.e("DataBaseRep", "The Fcm Data Couldnt Saved ${e.message}")
            }
        }
    }

    suspend fun saveSignIn(data: SignInData): Result<UserSession> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apis.signIn(data)
                Result.success(response)

            } catch (e: Exception) {
                Log.e("DataBaseRep", "The issue For Saving SignIn Details ${e.message}")
                Result.failure(e)
            }
        }

    }

    suspend fun saveBio(token: String, bioData: BioData, uid: String): Result<BioData> {
        return withContext(Dispatchers.IO) {
          safeApiCall(uid){
              apis.saveBio(token,bioData)
          }
        }

    }

    suspend fun getBio(token: String, uid: String): Result<BioData> {
        return withContext(Dispatchers.IO) {
            safeApiCall(uid){
                apis.getBio(token)
            }

        }
    }

    suspend fun check(token: String, uid: String): Result<Info> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apis.checking(token)
                Log.e("DataBaseRep", "info i got ${response.token}")
                Result.success(response)
            } catch (e: HttpException) {
                Log.e("DataBaseRep", "The Reason U cant Get ${e.message}")
                if (e.code() == 401) {
                    try {
                        val token = sharedPreferences.getRefreshToken(uid) ?: ""
                        val response = apis.refreshToken(Info(token))
                        Log.e(
                            "DataBaseRep",
                            " the Tokens I got From Sever ${response.token} and ${response.refreshToken}"
                        )
                        sharedPreferences.saveAccessToken(uid, response.token)
                        sharedPreferences.saveRefreshToken(uid, response.refreshToken)
                        Log.e(
                            "DataBaseRep",
                            "The Token Saved in Local ${sharedPreferences.getAccessToken(uid)} and ${response.refreshToken}"
                        )
                        val retry = apis.checking("Bearer ${response.token}")
                        Log.e("DataBaseRep", retry.token)
                        Result.success(retry)
                    } catch (e: Exception) {
                        Log.e("DataBaseRep", "The Issue For Retry Was ${e.message}")
                        Result.failure(e)
                    }

                }
                Result.failure(e)
            }
        }

    }

    suspend fun loginCheck(data: loginData): Result<UserSession> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apis.login(data)
                Result.success(response)

            } catch (e: Exception) {
                Log.e("DataBaseRep", "The issue For Saving SignIn Details ${e.message}")
                Result.failure(e)
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun saveImage(
        bytes: ByteArray,
        token: String,
        uid: String
    ): Result<com.example.chatapplication.repository.ImgBBResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = MyHttpClient.myClient.post(
                    "${UrlConstants.emUrl}/profile/saveImage"
                ) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                        append(HttpHeaders.ContentType, "application/json")
                    }
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    key = "image",
                                    value = bytes,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentType, "image/jpeg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "form-data; name=\"image\"; filename=\"image.jpg\""
                                        )
                                    }
                                )


                            }
                        )

                    )

                }.body<com.example.chatapplication.repository.ImgBBResponse>()
                Log.e("DataBaseRep", "${response}")
                Result.success(response)

            } catch (e: HttpException) {
                Log.e("DataBaseRep", "The Reason  cant send ${e.message}")
                if (e.code() == 401) {
                    try {
                        val token = sharedPreferences.getRefreshToken(uid) ?: ""
                        val response = apis.refreshToken(Info(token))
                        Log.e(
                            "DataBaseRep",
                            " the Tokens I got From Sever ${response.token} and ${response.refreshToken}"
                        )
                        sharedPreferences.saveAccessToken(uid, response.token)
                        sharedPreferences.saveRefreshToken(uid, response.refreshToken)
                        Log.e(
                            "DataBaseRep",
                            "The Token Saved in Local ${sharedPreferences.getAccessToken(uid)} and ${response.refreshToken}"
                        )
                        val retry = MyHttpClient.myClient.post(
                            "${UrlConstants.emUrl}/profile/saveImage"
                        ) {
                            headers {
                                append(HttpHeaders.Authorization, "Bearer $token")
                                append(HttpHeaders.ContentType, "application/json")
                            }
                            setBody(
                                MultiPartFormDataContent(
                                    formData {
                                        append(
                                            key = "image",
                                            value = bytes,
                                            headers = Headers.build {
                                                append(HttpHeaders.ContentType, "image/jpeg")
                                                append(
                                                    HttpHeaders.ContentDisposition,
                                                    "form-data; name=\"image\"; filename=\"image.jpg\""
                                                )
                                            }
                                        )


                                    }
                                )

                            )

                        }.body<com.example.chatapplication.repository.ImgBBResponse>()
                        Log.e("DataBaseRep", "${retry}")
                        Result.success(response)
                    } catch (e: Exception) {
                        Log.e("DataBaseRep", "The Issue For Retry Was ${e.message}")
                    }

                }
                Result.failure(e)
            }


        }

    }

    suspend fun getUser(token: String, uid: String): Result<WholeUser> {
        return withContext(Dispatchers.IO) {
            safeApiCall(uid){
                apis.getUserData(token)
            }


        }
    }

    suspend fun getAllUsers(token: String, uid: String): Result<List<WholeUser>> {
        return withContext(Dispatchers.IO) {
            safeApiCall(uid){
                apis.getAllUsers(token)
            }


        }
    }

    suspend fun followUser(
        followingUid: String,
        followerUid: String, token: String
    ): Result<FollowState> {
        return withContext(Dispatchers.IO) {
        safeApiCall(followerUid){
            apis.followUser(token,followingUid)
        }
    }
    }

    suspend fun acceptFollow(receiverUid: String, senderUid: String) {
        try {
            val token = sharedPreferences.getAccessToken(receiverUid) ?: ""
            val response = apis.acceptReq("Bearer $token", senderUid)

            Log.e("DataBaseRep", "Accepted ${response}")
        } catch (e: HttpException) {
            Log.e("DataBaseRep", "Issue accepting user: ${e.message}")
            if (e.code() == 401) {
                try {
                    val refreshToken = sharedPreferences.getRefreshToken(receiverUid) ?: ""
                    val response = apis.refreshToken(Info(refreshToken))
                    sharedPreferences.saveAccessToken(receiverUid, response.token)
                    sharedPreferences.saveRefreshToken(receiverUid, response.refreshToken)

                    val retry = apis.acceptReq("Bearer ${response.token}", senderUid)
                    Log.e("DataBaseRep", "Retry accepted: $retry")
                } catch (e: Exception) {
                    Log.e("DataBaseRep", "Retry failed: ${e.message}")
                }
            }
        }
    }

    suspend fun rejectFollow(receiverUid: String, senderUid: String) {
        try {
            val token = sharedPreferences.getAccessToken(receiverUid) ?: ""
            val response = apis.rejectReq("Bearer $token", senderUid)
            Log.e("DataBaseRep", "rejected $response")
        } catch (e: HttpException) {
            Log.e("DataBaseRep", "Issue rejecting user: ${e.message}")
            if (e.code() == 401) {
                try {
                    val refreshToken = sharedPreferences.getRefreshToken(receiverUid) ?: ""
                    val response = apis.refreshToken(Info(refreshToken))
                    sharedPreferences.saveAccessToken(receiverUid, response.token)
                    sharedPreferences.saveRefreshToken(receiverUid, response.refreshToken)

                    val retry = apis.rejectReq("Bearer ${response.token}", senderUid)
                    Log.e("DataBaseRep", "Retry rejected: $retry")
                } catch (e: Exception) {
                    Log.e("DataBaseRep", "Retry failed: ${e.message}")
                }
            }
        }
    }

    suspend fun followResponse(uid: String, token: String,currUid: String): Result<FollowState> {
        return withContext(Dispatchers.IO) {
            safeApiCall(currUid){
                apis.getFollowState(token,uid)
            }
        }
    }

    suspend fun getFollowers(uid: String, token: String): Result<FollowInfo> {
        return withContext(Dispatchers.IO) {
            safeApiCall(uid){
                apis.getFollowers(token)
            }
        }
    }

    suspend fun getFollowing(uid: String, token: String): Result<FollowInfo> {
        return withContext(Dispatchers.IO) {
            safeApiCall(uid){
                apis.getFollowing(token)
            }
        }
    }

    suspend fun getUserByUid(uid: String, token: String, currUid: String): Result<WholeUser> {
        return withContext(Dispatchers.IO) {
            safeApiCall(currUid) {
                apis.getUserByUid(token,uid)
            }
        }

    }

    suspend fun isOnline(uid:String,token: String,following_Uid: String):Result<isOnline>{
      return withContext(Dispatchers.IO){
   safeApiCall(uid){
       apis.isOnline(token,following_Uid)
   }
        }
    }
    suspend fun  getMessages(chat_id: String,token: String,currUid: String):Result<List<ChatData>>{
       return  withContext(Dispatchers.IO){
           safeApiCall(currUid){
               apis.getMessages(token,chat_id)
           }

        }
    }
    suspend fun sendMessageToAi(uid:String,token:String,message: String):Result<MessageData>{
        return withContext(Dispatchers.IO){
            safeApiCall(uid){
                apis.sendMessage(token, message = MessageData((message)))
            }

        }
    }
    @OptIn(InternalAPI::class)
    suspend fun uploadStatus(
        bytes: ByteArray,
        token: String,
        uid: String
    ): Result<ImgBBResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = MyHttpClient.myClient.post(
                    "${UrlConstants.emUrl}/status/uploadStatus"
                ) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                    }
                    setBody(
                        MultiPartFormDataContent(
                            formData {
                                append(
                                    key = "image",
                                    value = bytes,
                                    headers = Headers.build {
                                        append(HttpHeaders.ContentType, "image/jpeg")
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "form-data; name=\"image\"; filename=\"image.jpg\""
                                        )
                                    }
                                )
                            }
                        )

                    )

                }.body<ImgBBResponse>()
                Log.e("DataBaseRep", "${response}")
                Result.success(response)

            } catch (e: HttpException) {
                Log.e("DataBaseRep", "The Reason  cant send ${e.message}")
                if (e.code() == 401) {
                    try {
                        val token = sharedPreferences.getRefreshToken(uid) ?: ""
                        val response = apis.refreshToken(Info(token))
                        Log.e(
                            "DataBaseRep",
                            " the Tokens I got From Sever ${response.token} and ${response.refreshToken}"
                        )
                        sharedPreferences.saveAccessToken(uid, response.token)
                        sharedPreferences.saveRefreshToken(uid, response.refreshToken)
                        Log.e(
                            "DataBaseRep",
                            "The Token Saved in Local ${sharedPreferences.getAccessToken(uid)} and ${response.refreshToken}"
                        )
                        val retry = MyHttpClient.myClient.post(
                            "${UrlConstants.emUrl}/status/uploadStatus"
                        ) {
                            headers {
                                append(HttpHeaders.Authorization, "Bearer $token")
                            }
                            setBody(
                                MultiPartFormDataContent(
                                    formData {
                                        append(
                                            key = "image",
                                            value = bytes,
                                            headers = Headers.build {
                                                append(HttpHeaders.ContentType, "image/jpeg")
                                                append(
                                                    HttpHeaders.ContentDisposition,
                                                    "form-data; name=\"image\"; filename=\"image.jpg\""
                                                )
                                            }
                                        )
                                    }
                                )

                            )

                        }.body<ImgBBResponse>()
                        Log.e("DataBaseRep", "${retry}")
                        Result.success(response)
                    } catch (e: Exception) {
                        Log.e("DataBaseRep", "The Issue For Retry Was ${e.message}")
                    }

                }
                Result.failure(e)
            }


        }

    }
    suspend fun  getStatusImage(uid: String,token: String):Result<List<StatusModel>>{
       return  withContext(Dispatchers.IO) {
           safeApiCall(uid){
               apis.getStatusImage(token)

       }
       }


    }
    suspend fun  getFollowerStatus(uid: String,token: String):Result<List<StatusWithUser>>{
        return  withContext(Dispatchers.IO){
            safeApiCall(uid){
                apis.getFollowersStatus(token) }
        }
    }
    suspend fun deleteChat(uid: String,chat_id: String,token: String){
        withContext(Dispatchers.IO){
            safeApiCall(uid){
                apis.ClearChat(token,chat_id)
            }
        }
    }
    suspend fun deleteMessage(message_id: String,uid: String,token: String){
        withContext(Dispatchers.IO){
            safeApiCall(uid){
                apis.deleteMessage(token,message_id)
            }
        }
    }



}

@Serializable
data class ImgBBResponse(
    val data: ImgData,
    val success: Boolean,
    val status: Int
)
@Serializable
data class ImgData(
    val url: String
)
data class MessageData(
    val message: String
)