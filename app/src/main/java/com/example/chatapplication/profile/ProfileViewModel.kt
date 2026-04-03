package com.example.chatapplication.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.FollowState
import com.example.chatapplication.auth.LoginScreen
import com.example.chatapplication.models.BioData
import com.example.chatapplication.models.UserData
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.models.isOnline
import com.example.chatapplication.prefernces.SharedPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val dataBaseRep: DataBaseRep,
    val fbAuth: FirebaseAuth,
    val sharedPreferences: SharedPreferences,
    val context: Context
) : ViewModel(){
  private  val _bioState= MutableStateFlow("")
    val BioState=_bioState
    val UserInfo=MutableStateFlow<UserData?>(null)
    val user_Info=UserInfo

    fun saveBio(bio: String){
        viewModelScope.launch {
            try {
                val tokenFromPrefs = sharedPreferences.getAccessToken(currentUid()) ?: ""
                val bearerToken = "Bearer $tokenFromPrefs"
                val response = dataBaseRep.saveBio(
                    bearerToken,
                    BioData(
                        currentUid(),
                        bio
                    ),
                    currentUid()
                )
                response.onSuccess {
                    sharedPreferences.saveBio(it.uid, it.bio)
                    Log.e("Profile_VM", "${it.bio}")
                    _bioState.value = it.bio
                    loadUser()
                }
            }catch (e: Exception){
                Log.e("profile_Vm","The Issue For Not Saving Bio ${e.message}")
            }

        }

    }
    fun uriToByteArray(uri: Uri):ByteArray{
        val inputStream=context.contentResolver.openInputStream(uri)
        val byteArrayOutPutArray= ByteArrayOutputStream()
         inputStream?.copyTo(byteArrayOutPutArray)
        return byteArrayOutPutArray.toByteArray()
    }
    fun  saveImage(uri: Uri){
        try {
            viewModelScope.launch {
                val bytes = uriToByteArray(uri)
                val token = sharedPreferences.getAccessToken(currentUid()) ?: ""
                dataBaseRep.saveImage(bytes, token, currentUid())
            }
        }catch (e: Exception){
            Log.e("Profile_Vm","The Reason couldnt Save Image ${e.message}")
        }
    }
    fun loadBioFromPrefs() {
        val bio = sharedPreferences.getBio(currentUid()) ?: ""
        _bioState.value = bio
    }
    private val userName=MutableStateFlow("")
    val  name=userName
    fun loadName(){
        val uid=currentUid()
        val name=sharedPreferences.getName(uid)?:""
        Log.e("Profile_VM","${name}")
        userName.value=name
    }
    private val image_Url=MutableStateFlow("")
    val ImageUrl=image_Url

    fun loadImage(){
        val uid=currentUid()
        val url=sharedPreferences.getImage(uid)?:""
        Log.e("Profile_VM","${url}")
        image_Url.value=url
    }

    private fun currentUid(): String {
        return fbAuth.currentUser?.uid ?: ""
    }
    private val whole_user=MutableStateFlow<WholeUser?>(null)
    val wholeUser=whole_user
    fun loadUser(){
        val uid=currentUid()
        viewModelScope.launch {
            try {
                val token = sharedPreferences.getAccessToken(uid) ?: ""
                val bearerToken = "Bearer $token"
                val response = dataBaseRep.getUser(bearerToken, uid)
                response.onSuccess {
                    whole_user.value = it

                    Log.e("Profile_Vm", "The Response From databaseRep ${it}")

                }
            }catch(e: Exception){
                Log.e("Profile_Vm","The Issue to Fetch user Was ${e.message}")
            }
        }
    }
    init {

        loadUser()
    }
    private val allUsers=MutableStateFlow<List<WholeUser>>(emptyList())
    val AllUsers=allUsers
    fun loadAllUsers(){
        viewModelScope.launch {
            try {
                val uid = currentUid()
                val token = sharedPreferences.getAccessToken(uid) ?: ""
                val bearerToken = "Bearer $token"
                val response = dataBaseRep.getAllUsers(bearerToken, uid)
                response.onSuccess {
                    Log.e("profile_Vm","Successfully got users")
                     allUsers.value=it
                }
                response.onFailure {
                    Log.e("profile_Vm","The Issue of Not  Get All Users ${it.message}")

                }
            }catch (e: Exception){
                Log.e("profile_Vm","The Issue of Not  Get All Users ${e.message}")
            }
        }

    }

    fun followUser(followingUid: String){
        try {
            viewModelScope.launch {

                val curUid = currentUid()
                val token = sharedPreferences.getAccessToken(curUid) ?: ""
                val bearerToken = "Bearer $token"

                val response = dataBaseRep.followUser(followingUid, curUid, bearerToken)

                response.onSuccess { result ->

                    _followStates.value =
                        _followStates.value.toMutableMap().apply {
                            put(followingUid, result.state)
                        }
                }
                response.onFailure { result ->

                    _followStates.value =
                        _followStates.value.toMutableMap().apply {
                            put(followingUid, "Follow")
                        }
                }
            }
        }catch (e: Exception){
            Log.e("Profile_Vm","${e.message}")
        }
    }

    private val _followStates =
        MutableStateFlow<Map<String,String>>(emptyMap())

    val followStates = _followStates.asStateFlow()

    fun followState(followingUid: String){
        try{
            viewModelScope.launch {
                val curUid = currentUid()
                val token = sharedPreferences.getAccessToken(curUid) ?: ""
                val bearerToken = "Bearer $token"
                val response = dataBaseRep.followResponse(followingUid, bearerToken,curUid)
                response.onSuccess { state ->

                    _followStates.value =
                        _followStates.value.toMutableMap().apply {
                            put(followingUid, state.state)
                        }
                }

                response.onFailure {
                    _followStates.value =
                        _followStates.value.toMutableMap().apply {
                            put(followingUid, "Follow")
                        }
                }
            }
        }catch (e: Exception){
            Log.e("Profile_Vm","cannot get followState ${e.message}")
        }



    }
    private val follow_Count=MutableStateFlow<Int>(0)
    val followerCount=follow_Count
    private val followersInfo=MutableStateFlow<List<WholeUser>>(emptyList())
    val FollowersInfo=followersInfo
    fun getFollowers(){
        viewModelScope.launch {
            try {
                val curUid = currentUid()
                val token = sharedPreferences.getAccessToken(curUid) ?: ""
                val bearerToken = "Bearer $token"
                val response=dataBaseRep.getFollowers(uid = curUid, token = bearerToken)
                response.onSuccess {
                    follow_Count.value=it.size
                    followersInfo.value=it.data
                }
                response.onFailure {
                    follow_Count.value=0;
                    Log.e("Profile_Vm","the reason to not Fetch follow count ${it.message}")

                }

            }catch (e: Exception){
                Log.e("Profile_Vm","the reason to not Fetch follow count ${e.message}")
            }
        }
    }
    private val followingCount=MutableStateFlow<Int>(0)
    val FollowingCount=followingCount
    private val followingInfo=MutableStateFlow<List<WholeUser>>(emptyList())
    val FollowingInfo=followingInfo
    fun getFollowing(){
        viewModelScope.launch {
            try {
                val curUid = currentUid()
                val token = sharedPreferences.getAccessToken(curUid) ?: ""
                val bearerToken = "Bearer $token"
                val response=dataBaseRep.getFollowing(curUid,bearerToken)
                response.onSuccess {
                    followingCount.value=it.size
                    followingInfo.value=it.data
                }
                response.onFailure {
                    follow_Count.value=0;
                    Log.e("Profile_Vm","the reason to not Fetch follow count ${it.message}")

                }

            }catch (e: Exception){
                Log.e("Profile_Vm","the reason to not Fetch follow count ${e.message}")
            }
        }
    }
    private val status=MutableStateFlow<isOnline?>(null)
    val isOnline=status.asStateFlow()
    fun checkStatus(following_Uid: String){
        viewModelScope.launch {
            val CurUid=currentUid()

            val token=sharedPreferences.getAccessToken(CurUid)
            val bearerToken="Bearer ${token}"
            val response=dataBaseRep.isOnline(CurUid,bearerToken,following_Uid)
            response.onSuccess {
                Log.e("Profile_Vm","the Status Was ${it.text}")
                status.value=it
            }
            response.onFailure {
                Log.e("Profile_Vm","The Status Was${it.message}")
            }
        }
    }






}
