package ttpicshk.tk.SkillCirlce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import ttpicshk.tk.SkillCirlce.databinding.LoginLayoutBinding

class LogInActivity : AppCompatActivity() {
    lateinit var binding:LoginLayoutBinding

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

        //未完成
        /*binding.showPasswordPasswordLogin.setOnTouchListener { v, event ->
            if(event= MotionEvent.ACTION_DOWN)
                binding.passwordText.visibility= View.VISIBLE
        }*/


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}