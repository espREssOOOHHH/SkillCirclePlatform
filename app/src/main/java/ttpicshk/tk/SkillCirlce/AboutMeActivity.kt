package ttpicshk.tk.SkillCirlce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCirlce.databinding.AboutMeAccountSettingsBinding
import ttpicshk.tk.SkillCirlce.databinding.AboutMeLayoutBinding
import ttpicshk.tk.SkillCirlce.databinding.AboutMeSettingsPersonalInfoBinding

class AboutMeActivity : AppCompatActivity() {
    lateinit var binding:AboutMeLayoutBinding
    lateinit var bindingPersonalInfo:AboutMeSettingsPersonalInfoBinding
    lateinit var bindingAccountSettings:AboutMeAccountSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AboutMeLayoutBinding.inflate(layoutInflater)
        bindingPersonalInfo= AboutMeSettingsPersonalInfoBinding.inflate(layoutInflater)
        bindingAccountSettings= AboutMeAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(!Account.IsOnLine())
        {
            "请先登录".showToast(this)
            finish()
            startActivity(Intent(this,LogInActivity::class.java))
        }

        setSupportActionBar(binding.toolbarAboutMe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBarAboutMe.title=Account.userName()
        Glide.with(this).load(Account.userPhoto()).into(binding.userPhotoAboutMe)
        bindingPersonalInfo.aboutMeUsername.text=Account.userName()
        bindingPersonalInfo.aboutMeSignature.text=Account.signature()
        bindingPersonalInfo.aboutMeBirthday.text=Account.birthday()
        bindingPersonalInfo.aboutMeGender.text=Account.gender()
        bindingPersonalInfo.aboutMeId.text=Account.id()




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