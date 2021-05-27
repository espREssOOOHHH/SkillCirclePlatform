package ttpicshk.tk.SkillCirlce

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.GravityCompat
import ttpicshk.tk.SkillCirlce.databinding.LoginLayoutBinding

class LogInActivity : AppCompatActivity() {
    lateinit var binding:LoginLayoutBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.title="Login"


        binding.clearButtonUserNameLogin.setOnClickListener {
            binding.userNameText.text.clear()
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
                    setBackgroundResource(R.drawable.icon_eye)
                }
                MotionEvent.ACTION_DOWN->{
                    binding.passwordText.transformationMethod=
                        HideReturnsTransformationMethod.getInstance()
                    binding.showPasswordPasswordLogin.
                    setBackgroundResource(R.drawable.icon_eye_close)
                }
            }
            return@setOnTouchListener false
        }


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