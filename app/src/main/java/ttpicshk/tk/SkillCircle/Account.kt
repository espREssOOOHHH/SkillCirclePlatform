package ttpicshk.tk.SkillCircle

import android.net.Uri
import android.util.JsonToken
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread


object Account{
    private var userName ="user"
    private var passWord=String()
    private var userId=0
    private var phoneNumber= String()
    private var isLogin=false
    private var photoUser:Uri=Uri.parse("https://pic1.zhimg.com/v2-5ec0fb527c389530021ec2c911875165_r.jpg?source=1940ef5c")
    private var signature="fuck U asshole"
    private var birthday="2013-02-10"
    private var age=29
    private var gender=2
    private var occupation=""
    private var location=""
    private var id=0
    private var EmotionalState=0
    private var email="xxyyy@ss.com"
    private var phone="0000000000000"
    private var photoBg:Uri=Uri.parse("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7")
    lateinit var token: String

    fun setPersonalInfo(id_:Int,userId_:Int,userName_:String?,age_:Int,sex_:Int,gq:Int,job:String?,path:String?,
    birthday_:String?,signature_:String?){
        id=id_
        userName=userName_?: userName
        userId=userId_
        age=age_
        EmotionalState=gq
        gender=sex_
        occupation=job?: occupation
        location=path?: location
        birthday=birthday_?: birthday
        signature=signature_?: signature
    }
    fun userName():String= userName
    fun userPhoto(photo:Uri?=null):Uri{
        if(photo!=null)
            photoUser=photo
        return photoUser
    }
    fun signature():String= signature
    fun birthday():String= birthday
    fun id()= id
    fun email():String=email
    fun phone():String=phone
    fun age():Int=age
    fun location()= location
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

    fun LogIn(tokens:String){
        token=tokens
        isLogin=true
    }

    fun LogOut():Boolean{
        thread {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = "".toRequestBody(mediaType)
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/logout")
                .method("POST", body)
                .addHeader("token", token)
                .build()
            val response = client.newCall(request).execute()
            Log.d("login_network", response.body!!.string())
            if(response.isSuccessful){
                isLogin=false
            }
        }
        return true
    }

    fun IsOnLine():Boolean{
        return isLogin
    }

}