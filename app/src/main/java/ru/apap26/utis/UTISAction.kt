package ru.apap26.utis

import android.util.Log
import okhttp3.*
import java.net.ConnectException
import java.net.UnknownServiceException


class UTISAction (login:String, password:String){
    private val LOGIN_PARAM = "login"
    private val PASSWORD_PARAM = "password"
    private val client:OkHttpClient = OkHttpClient()
    private val login = login
    private val password = password
    private lateinit var session_id:String
    private var url:String = "http://192.168.73.2:8000/api/"
    public fun auth():Boolean{
        val AUTH_URL = url + "auth/"
        val requestBody =  MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(LOGIN_PARAM, login)
            .addFormDataPart(PASSWORD_PARAM, password)
            .build()
        val request:Request = Request.Builder()
            .url(AUTH_URL)
            .post(requestBody)
            .build()

        val response:Response = client.newCall(request).execute()
        val strResponse:String = (response.body?.string() ?: "Ублюдок мать твою, а ну иди сюда! го")
        Log.d("UTISAction","body is: "+ strResponse)
        return strResponse.length == 52
    }
    public fun getTimetable(week:Int){

    }
    fun changeApiServer(url:String){
        this.url = url
    }
    private fun sendToServer(urls:String, x:Double, y:Double, z:Double, smp:Int){
        val url = urls + "?smp="+ smp + "&x=" + x + "&y=" + y +"&z=" + z
        try {
            val request:Request = Request.Builder().url(url).build()
            val response:Response = client.newCall(request).execute()
            val text = response.body?.string() ?: "error"
        }catch (e: UnknownServiceException){
            Log.d("nett", "Responce, UnknownServiceException")

        }
    }
}