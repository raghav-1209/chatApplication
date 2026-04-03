package com.example.chatapplication.status

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.models.StatusModel
import com.example.chatapplication.models.StatusWithUser
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.prefernces.SharedPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    val fbAuth: FirebaseAuth,
  val sharedPreferences: SharedPreferences,
    val context: Context,
  val dataBaseRep: DataBaseRep): ViewModel(){
      private  val imageUrl= MutableStateFlow<List<StatusModel>>(emptyList())
    val ImageURL=imageUrl
     fun currUid(): String {
        return fbAuth.currentUser?.uid ?: ""
    }

    fun saveImage(uri: Uri,uid: String){
        try {
            viewModelScope.launch {
                val bytes = uriToByteArray(uri)
                val token = sharedPreferences.getAccessToken(uid) ?: ""
                val response=dataBaseRep.uploadStatus(bytes, token, currUid())
                response.onSuccess {
                    Log.e("Status_VM","${it}")
                    getStatusImage()

                }
            }
        }catch (e: Exception){
            Log.e("Status_vm","The iSsue Couldnt save Was ${e.message}")
        }
    }
   private  val _userInfo = MutableStateFlow<WholeUser?>(null)
    val userInfo=_userInfo.asStateFlow()
    fun getUserInfo(uid: String){
        viewModelScope.launch {
            val token = sharedPreferences.getAccessToken(currUid()) ?: ""
            val bearerToken="Bearer ${token}"
            val response=dataBaseRep.getUser(bearerToken,uid)
            response.onSuccess {
                _userInfo.value=it
            }
            response.onFailure {
                Log.e("Status_vm","The iSsue Cannot get User Info save Was ${it}")


            }
        }

    }

    fun getStatusImage(){
        viewModelScope.launch {
            val uid=currUid()
            val token = sharedPreferences.getAccessToken(uid) ?: ""
            val bearerToken = "Bearer $token"
            val response = dataBaseRep.getStatusImage(uid,bearerToken)
            response.onSuccess {
                Log.e("Status_Vm","${it}")
                imageUrl.value=it
            }
            response.onFailure {
                Log.e("Status_Vm","The reason cannot get Status ${it}")
            }
        }

    }
    private val _selectedStatuses = MutableStateFlow<List<StatusWithUser>>(emptyList())
    val selectedStatuses = _selectedStatuses

    fun setSelectedStatuses(statuses: List<StatusWithUser>) {
        _selectedStatuses.value = statuses
    }
    private  val FollowingStatus=MutableStateFlow<List<StatusWithUser>>(emptyList())
    val FollowingStatus_=FollowingStatus
    fun getFollowersStatus(){
        viewModelScope.launch {
            val uid=currUid()
            val token = sharedPreferences.getAccessToken(uid) ?: ""
            val bearerToken = "Bearer $token"
            val response = dataBaseRep.getFollowerStatus(uid,bearerToken)
            response.onSuccess {
                Log.e("Status_Vm","${it}")
                FollowingStatus.value=it
            }
            response.onFailure {
                Log.e("Status_Vm","The reason cannot get Following StatusStatus ${it}")
            }

        }

    }

    fun uriToByteArray(uri: Uri):ByteArray{
        val inputStream=context.contentResolver.openInputStream(uri)
        val byteArrayOutPutArray= ByteArrayOutputStream()
        inputStream?.copyTo(byteArrayOutPutArray)
        return byteArrayOutPutArray.toByteArray()
    }

}