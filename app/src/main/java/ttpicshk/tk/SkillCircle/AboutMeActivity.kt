package ttpicshk.tk.SkillCircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCircle.databinding.AboutMeAccountSettingsBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsPersonalInfoBinding

class AboutMeActivity : AppCompatActivity() {
    lateinit var binding:AboutMeLayoutBinding
    lateinit var bindingPersonalInfo:AboutMeSettingsPersonalInfoBinding
    lateinit var bindingAccountSettings:AboutMeAccountSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AboutMeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingPersonalInfo=binding.aboutMeSettingsPersonalInfo
        bindingAccountSettings=binding.aboutMeAccountSettings


        if(!Account.IsOnLine())
        {
            "请先登录".showToast(this)
            finish()
            startActivity(Intent(this,LogInActivity::class.java))
        }

        setSupportActionBar(binding.toolbarAboutMe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBarAboutMe.title=Account.userName()
        Glide.with(this).load(R.drawable.apple).into(binding.userPhotoAboutMe)
        binding.userPhotoAboutMe.setImageResource(Account.userPhoto())

        bindingPersonalInfo.aboutMeUsername.text=Account.userName()
        bindingPersonalInfo.aboutMeSignature.text=Account.signature()
        bindingPersonalInfo.aboutMeBirthday.text=Account.birthday()
        bindingPersonalInfo.aboutMeGender.text=Account.gender()
        bindingPersonalInfo.aboutMeId.text=Account.id()
        bindingAccountSettings.aboutMeEmail.text=Account.email()
        bindingAccountSettings.aboutMePhone.text=Account.phone()




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