package ttpicshk.tk.SkillCircle

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import androidx.core.graphics.toColor
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutCreateUserBinding
import ttpicshk.tk.SkillCircle.databinding.LoginLayoutUseMessageBinding

class LogInActivity : AppCompatActivity() {
    lateinit var binding:LoginLayoutBinding
    lateinit var binding2:LoginLayoutUseMessageBinding
    lateinit var binding3:LoginLayoutCreateUserBinding

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
                if(logIn()){
                    "登陆成功！".showToast(this)
                    finish()
                }
                else{
                    "账号或密码错误".showToast(this)
                }
            }
        }
        binding2.buttonLogin.setOnClickListener {
            if(binding2.userNameText.text.isEmpty()){
                "请输入手机号".showToast(AllApplication.context)
            }
            else if(!binding2.checkedAgreePolicy.isChecked){
                "请阅读并同意用户协议".showToast(AllApplication.context)
                showPolicy(this,binding2.checkedAgreePolicy)
            }
            else{
                    "登陆成功！".showToast(this)
                    finish()

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
                            "注册成功！".showToast(AllApplication.context)
                            toLoginUseUsernameAndPassword()
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

    private fun toLoginUseUsernameAndPassword(){
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title="登录"
        }
    }
    private fun toLoginUsePhone(){
        setContentView(binding2.root)
        setSupportActionBar(binding2.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title="手机号登录"
        }
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
        text_policy.text="sjdfgjdsf"
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

    private fun logIn():Boolean{
        return Account.LogIn(binding.userNameText.text.toString(),
            binding.passwordText.text.toString(),"12hefwjk")
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