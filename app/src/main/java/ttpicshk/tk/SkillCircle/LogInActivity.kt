package ttpicshk.tk.SkillCircle

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.JsonToken
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import ttpicshk.tk.SkillCircle.AllApplication.Companion.context
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutCreateUserBinding
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutUseMessageBinding
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.properties.Delegates



class LogInActivity : AppCompatActivity() {
    lateinit var binding:LoginLayoutBinding
    lateinit var binding2:LoginLayoutUseMessageBinding
    lateinit var binding3:LoginLayoutCreateUserBinding

    var messageLoginState=0//用于短信验证码登录状态验证

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoginLayoutBinding.inflate(layoutInflater)
        binding2= LoginLayoutUseMessageBinding.inflate(layoutInflater)
        binding3= LoginLayoutCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toLoginUseUsernameAndPassword()

        //头像
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding.logoLogin)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding2.logoLogin)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding3.logoLogin)

        //stub 仅供测试用
        binding.logoLogin.setOnClickListener {
            Account.LogIn("王琳琳","10293221","123938231203")
            finish()
        }


        //输入框清空按钮
        binding.clearButtonUserNameLogin.setOnClickListener {
            binding.userNameText.text.clear()
        }
        binding2.clearButtonUserNameLogin.setOnClickListener {
            binding2.userNameText.text.clear()
        }
        binding.clearButtonPasswordLogin.setOnClickListener {
            binding.passwordText.text.clear()
        }
        binding3.clearButtonUserNameLogin.setOnClickListener {
            binding3.userNameText.text.clear()
        }
        binding3.clearButtonPasswordLogin.setOnClickListener {
            binding3.passwordText.text.clear()
        }
        binding3.clearButtonPasswordLoginConfirm.setOnClickListener {
            binding3.passwordTextConfirm.text.clear()
        }

        //密码显示按钮
        binding.showPasswordPasswordLogin.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_UP->{
                    binding.passwordText.transformationMethod=
                        PasswordTransformationMethod.getInstance()
                    binding.showPasswordPasswordLogin.
                    setBackgroundResource(R.drawable.icon_eye_close)
                }
                MotionEvent.ACTION_DOWN->{
                    binding.passwordText.transformationMethod=
                        HideReturnsTransformationMethod.getInstance()
                    binding.showPasswordPasswordLogin.
                    setBackgroundResource(R.drawable.icon_eye)
                }
            }
            return@setOnTouchListener false
        }
        binding3.showPasswordPasswordLogin.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_UP->{
                    binding3.passwordText.transformationMethod=
                        PasswordTransformationMethod.getInstance()
                    binding3.showPasswordPasswordLogin.
                    setBackgroundResource(R.drawable.icon_eye_close)
                }
                MotionEvent.ACTION_DOWN->{
                    binding3.passwordText.transformationMethod=
                        HideReturnsTransformationMethod.getInstance()
                    binding3.showPasswordPasswordLogin.
                    setBackgroundResource(R.drawable.icon_eye)
                }
            }
            return@setOnTouchListener false
        }
        binding3.showPasswordPasswordLoginConfirm.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_UP->{
                    binding3.passwordTextConfirm.transformationMethod=
                        PasswordTransformationMethod.getInstance()
                    binding3.showPasswordPasswordLoginConfirm.
                    setBackgroundResource(R.drawable.icon_eye_close)
                }
                MotionEvent.ACTION_DOWN->{
                    binding3.passwordTextConfirm.transformationMethod=
                        HideReturnsTransformationMethod.getInstance()
                    binding3.showPasswordPasswordLoginConfirm.
                    setBackgroundResource(R.drawable.icon_eye)
                }
            }
            return@setOnTouchListener false
        }

        //用户协议显示
        binding.checkedTextview.setOnClickListener {
            showPolicy(this,binding.checkedAgreePolicy)
        }
        binding2.checkedTextview.setOnClickListener {
            showPolicy(this,binding2.checkedAgreePolicy)
        }
        binding3.checkedTextview.setOnClickListener {
            showPolicy(this,binding3.checkedAgreePolicy)
        }

        //登录按钮们
        binding.buttonLogin.setOnClickListener {
            if(binding.userNameText.text.isEmpty()){
                "请输入账户".showToast(AllApplication.context)
            }
            else if(binding.passwordText.text.isEmpty()){
                "请输入密码".showToast(AllApplication.context)
            }
            else if(!binding.checkedAgreePolicy.isChecked){
                "请阅读并同意用户协议".showToast(AllApplication.context)
                showPolicy(this,binding.checkedAgreePolicy)
            }
            else{
                Loading.start(this)
                loginUserName(binding.userNameText.text.toString(),
                    binding.passwordText.text.toString())
            }
        }
        binding2.buttonLogin.setOnClickListener {
            if(binding2.userNameText.text.isEmpty() or (binding2.userNameText.text.length<11)){
                "请输入正确的手机号".showToast(AllApplication.context)
            }
            else if(!binding2.checkedAgreePolicy.isChecked){
                "请阅读并同意用户协议".showToast(AllApplication.context)
                showPolicy(this,binding2.checkedAgreePolicy)
            }
            else{
                if(messageLoginState==0){
                    Loading.start(this)
                    sendMessage(binding2.userNameText.text.toString())
                }else {
                    if(binding2.CodeText.text.length<4)
                    {
                        "请输入正确的验证码！".showToast(AllApplication.context)
                    }else{
                        Loading.start(this)
                    loginPhoneNumber(binding2.userNameText.text.toString(),
                        binding2.CodeText.text.toString())
                    }
                }

            }
        }
        binding3.buttonLogin.setOnClickListener {
            if(checkCreateUser(binding3.userNameText.text.toString(),
                    binding3.passwordText.text.toString(),
                    binding3.passwordTextConfirm.text.toString())){
                        if(!binding3.checkedAgreePolicy.isChecked)
                        {
                            "请阅读并同意用户协议".showToast(AllApplication.context)
                            showPolicy(this,binding3.checkedAgreePolicy)
                        }
                        else{
                            Loading.start(this)
                            createUserLogin(binding3.userNameText.text.toString(),
                            binding3.passwordText.text.toString(),
                                binding3.passwordTextConfirm.text.toString())
                        }
            }
        }

        //切换登录方式
        binding.buttonLoginUseMessage.setOnClickListener {
            toLoginUsePhone()
        }
        binding2.buttonLoginUsePassword.setOnClickListener {
            toLoginUseUsernameAndPassword()
        }
        binding.createUserLogin.setOnClickListener {
            toCreateUser()
        }
        binding3.buttonLoginUsePassword.setOnClickListener {
            toLoginUseUsernameAndPassword()
        }

        //第三方登录
        binding.buttonWechatLogin.setOnClickListener {
            "微信登录成功！".showToast(this)
            finish()
        }
        binding2.buttonWechatLogin.setOnClickListener {
            "微信登录成功！".showToast(this)
            finish()
        }
        binding.buttonQqLogin.setOnClickListener {
            "QQ登录成功！".showToast(this)
            finish()
        }
        binding2.buttonQqLogin.setOnClickListener {
            "QQ登录成功！".showToast(this)
            finish()
        }
        binding.buttonWeiboLogin.setOnClickListener {
            "微博登录成功！".showToast(this)
            finish()
        }
        binding2.buttonWeiboLogin.setOnClickListener {
            "微博登录成功！".showToast(this)
            finish()
        }
        binding.problemButtonLogin.setOnClickListener {
            "问题是啥子？".showToast(this)
        }
        binding2.problemButtonLogin.setOnClickListener {
            "问题是啥子？".showToast(this)
        }
        binding3.problemButtonLogin.setOnClickListener {
            "问题是啥子？".showToast(this)
        }
    }

    val handler=object:Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            Loading.stop()
            val data:messageJson= msg.obj as messageJson
            when(msg.arg1){
                0->{"连接失败！".showToast(context)}
                1->{//login username
                    if(data.errorCode!=0){
                        "Oops! 登陆失败，原因为：${data.msg}".showToast(AllApplication.context)
                    }else{
                        runOnUiThread { "登陆成功！".showToast(AllApplication.context) }
                        Account.token= data.data.token
                        finish()
                    }
                }
                2->{//send message
                    if(data.errorCode!=0){
                        "Oops! 发送失败，错误原因：${data.msg}".showToast(AllApplication.context)
                    }
                    else{
                        runOnUiThread {
                            "发送成功，验证码是 ${data.data.code}".showToast(AllApplication.context,Toast.LENGTH_LONG);
                            binding2.messageFrameLogin.visibility = View.VISIBLE
                            messageLoginState = 1
                            binding2.buttonLogin.text="用短信验证码登录"
                        }
                    }
                }
                3->{//login phoneNumber
                    if(data.errorCode!=0){
                        "Oops!登陆失败,请重试！错误原因: ${data.msg}".showToast(AllApplication.context)
                    }
                    else{
                        Account.token= data.data.token
                         "登陆成功！".showToast(AllApplication.context)
                        finish()
                    }
                }
                4->{
                    if(data.errorCode!=0){
                        runOnUiThread { "注册失败，请重试，错误原因：${data.msg}".showToast(AllApplication.context) }
                    }else{
                        toLoginUseUsernameAndPassword()
                        "注册成功！".showToast(AllApplication.context)
                    }

                }
            }
        }
    }

    private fun createUserLogin(username: String,password: String,passwordR:String) {
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("repassword", passwordR)
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/reg")
                .method("POST", body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg = Message()
                    msg.arg1 = 0
                    msg.obj = messageJson()
                    handler.sendMessage(msg)
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, messageJson::class.java)
                    val msg = Message()
                    msg.arg1 = 4
                    msg.obj = message
                    handler.sendMessage(msg)
                }
            })
        }
    }

    inner class messageJson(){
        lateinit var msg:String
        var errorCode:Int=0
        var data=Data()
        inner class Data {
            var code:Int=0
            var token:String=""
        }
    }
    private fun sendMessage(phone: String) {
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder().connectTimeout(3,TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/sendcode")
                .method("POST", body)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg=Message()
                    msg.arg1=0
                    msg.obj=messageJson()
                    handler.sendMessage(msg)
                }
                override fun onResponse(call: Call, response: Response) {
                    val data=response.body!!.string()
                    Log.d("login_network",data)
                    val gson = Gson()
                    val message = gson.fromJson(data, messageJson::class.java)
                    val msg = Message()
                    msg.arg1 = 2
                    msg.obj = message
                    handler.sendMessage(msg)
                }
            })
        }
    }
    private fun loginPhoneNumber(phone: String, code: String) {
        thread {
            val client = OkHttpClient().newBuilder().connectTimeout(3,TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", phone)
                .addFormDataPart("code",code)
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/phonelogin")
                .method("POST", body)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg=Message()
                    msg.arg1=0
                    msg.obj=messageJson()
                    handler.sendMessage(msg)
                }

                override fun onResponse(call: Call, response: Response) {
                    val data=response.body!!.string()
                    Log.d("login_network",data)
                    val gson = Gson()
                    val message = gson.fromJson(data, messageJson::class.java)
                    val msg = Message()
                    msg.arg1 = 3
                    msg.obj = message
                    handler.sendMessage(msg)
                    if(response.isSuccessful){
                        Account.LogIn("0","0",phone)
                    }
                }
            })
        }
    }
    private fun loginUserName(username: String, password: String) {
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder().connectTimeout(3,TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/login")
                .method("POST", body)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg = Message()
                    msg.arg1 = 0
                    msg.obj = messageJson()
                    handler.sendMessage(msg)
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, messageJson::class.java)
                    val msg = Message()
                    msg.arg1 = 1
                    msg.obj = message
                    handler.sendMessage(msg)
                    if (response.isSuccessful) {
                        Account.LogIn(username, password, "0")
                    }
                }
            })
        }
    }

    //界面跳转
    private fun toLoginUseUsernameAndPassword(){
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title="登录"
        }
    }
    private fun toLoginUsePhone(){
        messageLoginState=0
        setContentView(binding2.root)
        setSupportActionBar(binding2.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title="手机号登录"
        }
        binding2.messageFrameLogin.visibility=View.GONE
    }
    private fun toCreateUser(){
        setContentView(binding3.root)
        setSupportActionBar(binding3.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title="注册用户"
        }
    }

    private fun checkCreateUser(username:String?,password:String?,
                                passwordConfirmed:String?): Boolean {
        if(username.isNullOrEmpty()){
            "请输入用户名".showToast(AllApplication.context)
            binding3.userNameText.hint="请输入合法的用户名"
            return false
        }
        if(password.isNullOrEmpty()){
            "请输入密码".showToast(AllApplication.context)
            binding3.passwordText.hint="请输入密码"
            return false
        }
        if(password.length<8)
        {
            "密码长度不合格".showToast(AllApplication.context)
            binding3.passwordText.hint="请输入密码"
            return false
        }
        if(passwordConfirmed.isNullOrEmpty()){
            "请确认密码".showToast(AllApplication.context)
            binding3.passwordTextConfirm.hint="请确认密码"
            return false
        }
        if(password!=passwordConfirmed){
            "确认密码和原密码不一致".showToast(AllApplication.context)
            binding3.passwordTextConfirm.hint="请输入密码"
            return false
        }
        return true
    }
    private fun showPolicy(context:Context,checkbox:CheckBox){
        val dialog= Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.user_policy)
        val text_policy=dialog.findViewById<TextView>(R.id.text_user_policy)
        text_policy.text="请您本产品之前，请务必仔细阅读" +
                "并理解《用户许可使用协议》（以下简称“本协议”）中规定的多有权利和限制。\n" +
                "\n" +
                "我们一向尊重并会严格保护用户在使用本产品时的合" +
                "法权益（包括用户隐私、用户数据等）不受到任何侵犯。\n" +
                "\n" +
                "本协议（包括本文最后部分的隐私政策）是用户（" +
                "包括通过各种合法途径获取到本产品的自然人、法人或其" +
                "他组织机构，以下简称“用户”或“您”）与我们之间针对本产品" +
                "相关事项最终的、完整的且排他的协议，并取代、合并之前的当事人" +
                "之间关于上述事项的讨论和协议。\n" +
                "\n" +
                "本协议将对用户使用本产品的行为产生法律约束力，您已承诺和保" +
                "证有权利和能力订立本协议。用户开始使用本产品将视为已经接受" +
                "本协议，请认真阅读并理解本协议中各种条款，包括免除和限制我们" +
                "的免责条款和对用户的权利限制（未成年人审阅时应由法定监护人陪" +
                "同），如果您不能接受本协议中的全部条款，请勿开始使用本产品。"
        val confirm_button=dialog.findViewById<Button>(R.id.agreeButton_user_policy)
        confirm_button.setOnClickListener {
            "已经同意用户协议".showToast(AllApplication.context)
            dialog.dismiss()
            checkbox.isChecked=true
        }
        val disagree_button=dialog.findViewById<Button>(R.id.disagreeButton_user_policy)
        disagree_button.setOnClickListener {
            "不同意用户协议".showToast(AllApplication.context)
            dialog.dismiss()
            checkbox.isChecked=false
        }
        dialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
            R.id.questionButtonToolbar->
                "问题？".showToast(this)
        }
        return super.onOptionsItemSelected(item)
    }
    //codes about menu button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toobar_login,menu)
        return true
    }


}