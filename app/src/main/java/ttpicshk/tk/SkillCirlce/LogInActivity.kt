package ttpicshk.tk.SkillCirlce

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import ttpicshk.tk.SkillCirlce.databinding.LoginLayoutBinding
import ttpicshk.tk.SkillCirlce.databinding.LoginLayoutUseMessageBinding

class LogInActivity : AppCompatActivity() {
    lateinit var binding:LoginLayoutBinding
    lateinit var binding2:LoginLayoutUseMessageBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoginLayoutBinding.inflate(layoutInflater)
        binding2= LoginLayoutUseMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.clearButtonUserNameLogin.setOnClickListener {
            binding.userNameText.text.clear()
        }
        binding2.clearButtonUserNameLogin.setOnClickListener {
            binding2.userNameText.text.clear()
        }
        binding.clearButtonPasswordLogin.setOnClickListener {
            binding.passwordText.text.clear()
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

        binding.checkedTextview.setOnClickListener {
            showPolicy(this,binding.checkedAgreePolicy)
        }
        binding2.checkedTextview.setOnClickListener {
            showPolicy(this,binding2.checkedAgreePolicy)
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
        //bug
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

        //切换登录方式
        binding.buttonLoginUseMessage.setOnClickListener {
            setContentView(binding2.root)
            setSupportActionBar(binding2.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        binding2.buttonLoginUsePassword.setOnClickListener {
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
            }
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