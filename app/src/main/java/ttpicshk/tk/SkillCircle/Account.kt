package ttpicshk.tk.SkillCircle

import android.net.Uri
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


object Account{
    private var userName ="user"
    private var passWord=String()
    private var phoneNumber= String()
    private var isLogin=false
    private var photoUser:Uri=Uri.parse("https://pic1.zhimg.com/v2-5ec0fb527c389530021ec2c911875165_r.jpg?source=1940ef5c")
    private var signature="fuck U asshole"
    private var birthday="2013-02-10"
    private var gender=2
    private var id="0"
    private var email="xxyyy@ss.com"
    private var phone="0000000000000"
    private var photoBg:Uri=Uri.parse("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7")

    fun userName():String= userName
    fun userPhoto(photo:Uri?=null):Uri{
        if(photo!=null)
            photoUser=photo
        return photoUser
    }
    fun signature():String= signature
    fun birthday():String= birthday
    fun id():String=id
    fun email():String=email
    fun phone():String=phone
    fun userBackGround(photo: Uri?=null):Uri{
        if(photo!=null)
            photoBg=photo
        return photoBg
    }

    fun gender():String{
        var backValue=""
        when(gender){
            0->backValue="男"
            1->backValue= "女"
            2->backValue="保密"
        }
        return backValue
    }

    fun LogIn(username:String,password:String,phone:String) :Boolean{
        isLogin=true
        userName=username
        passWord=password
        phoneNumber=phone
        return true
    }
    fun LogIn_UserName(username:String,password:String):Boolean{
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()
        val request: Request = Request.Builder()
            .url("http://api.classtest.com/user/login")
            .method("POST", body)
            .build()
        val response: Response = client.newCall(request).execute()
        return response.isSuccessful
    }
    fun LogOut():Boolean{
        /*val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = "text/plain".toMediaTypeOrNull()
        val body: RequestBody = "".toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .url("https://ceshi.299597.xyz/api/v1/post/logout")
            .method("POST", body)
            .addHeader("token", "9260ce339e57a03c4db73bc0d0ef438fba608be8")
            .build()
        val response = client.newCall(request).execute()
        return if(response.isSuccessful){
            isLogin=false
            true
        } else
            false*/
        isLogin=false
        return true
    }

    fun IsOnLine():Boolean{
        return isLogin
    }

}