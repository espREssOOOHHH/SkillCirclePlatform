package ttpicshk.tk.SkillCircle

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.pictureselector.GlideEngine
import ttpicshk.tk.SkillCircle.databinding.AboutMeAccountSettingsBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsMoreBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsPersonalInfoBinding
import kotlin.concurrent.thread

class AboutMeActivity : AppCompatActivity() {
    lateinit var binding:AboutMeLayoutBinding
    lateinit var bindingPersonalInfo:AboutMeSettingsPersonalInfoBinding
    lateinit var bindingAccountSettings:AboutMeAccountSettingsBinding
    lateinit var bindingSettingsMore: AboutMeSettingsMoreBinding

    var modePictureSelector=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AboutMeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingPersonalInfo=binding.aboutMeSettingsPersonalInfo
        bindingAccountSettings=binding.aboutMeAccountSettings
        bindingSettingsMore=binding.aboutMeSettingsMore

        if(!Account.IsOnLine())
        {
            "请先登录".showToast(this)
            finish()
            startActivity(Intent(this,LogInActivity::class.java))
        }

        setSupportActionBar(binding.toolbarAboutMe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBarAboutMe.title=Account.userName()
        Glide.with(this).load(Account.userBackGround()).into(binding.userPhotoBGAboutMe)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding.userPhotoAboutMe)
        binding.userPhotoAboutMe.setOnClickListener {
            val dialog= Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.photo_big_view)
            val picture=dialog.findViewById<ImageView>(R.id.pictureViewDialog)
            Glide.with(this).load(Account.userPhoto()).into(picture)
            dialog.show()
        }
        binding.userPhotoBGAboutMe.setOnClickListener {
            val dialog= Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.photo_big_view)
            val picture=dialog.findViewById<ImageView>(R.id.pictureViewDialog)
            Glide.with(this).load(Account.userBackGround()).into(picture)
            dialog.show()
        }

        bindingPersonalInfo.aboutMeUsername.text=Account.userName()
        bindingPersonalInfo.aboutMeSignature.text=Account.signature()
        bindingPersonalInfo.aboutMeBirthday.text=Account.birthday()
        bindingPersonalInfo.aboutMeGender.text=Account.gender()
        bindingPersonalInfo.aboutMeId.text=Account.id()
        bindingAccountSettings.aboutMeEmail.text=Account.email()
        bindingAccountSettings.aboutMePhone.text=Account.phone()

        bindingAccountSettings.aboutMePasswordSettingBtn.setOnClickListener{
            "设置密码".showToast(AllApplication.context)
        }
        bindingAccountSettings.aboutMePhoneBtn.setOnClickListener {
            "手机号".showToast(AllApplication.context)
        }
        bindingAccountSettings.aboutMeEmailBtn.setOnClickListener {
            "邮箱".showToast(AllApplication.context)
        }
        bindingAccountSettings.aboutMeWechatBtn.setOnClickListener {
            "微信".showToast(AllApplication.context)
        }
        bindingAccountSettings.aboutMeWeiboBtn.setOnClickListener {
            "微博".showToast(AllApplication.context)
        }
        bindingAccountSettings.aboutMeQqBtn.setOnClickListener {
            "QQ".showToast(AllApplication.context)
        }

        bindingSettingsMore.aboutMeLogout.setOnClickListener {
                Account.LogOut()
            "退出登录，请重新登陆".showToast(AllApplication.context)
            finish()
        }
        bindingSettingsMore.aboutMeUserCenter.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://baidu.com")
            startActivity(intent)
        }
        bindingPersonalInfo.aboutMeUsernameBtn.setOnClickListener {
            "Username".showToast(AllApplication.context)
        }
        bindingPersonalInfo.aboutMeIdBtn.setOnClickListener {
            "Id".showToast(AllApplication.context)
        }
        bindingPersonalInfo.aboutMeBirthdayBtn.setOnClickListener {
            "生日".showToast(AllApplication.context)
        }
        bindingPersonalInfo.aboutMeGenderBtn.setOnClickListener {
            "性别".showToast(AllApplication.context)
        }
        bindingPersonalInfo.aboutMeBirthdayBtn.setOnClickListener {

        }
        bindingPersonalInfo.aboutMeSignatureBtn.setOnClickListener {

        }
        bindingPersonalInfo.aboutMeUserPhotoBtn.setOnClickListener {
            modePictureSelector=1
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.Theme_SkillCirclePlatform)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(1)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }
        bindingPersonalInfo.aboutMeUserBackGroundBtn.setOnClickListener {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.Theme_SkillCirclePlatform)
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(1)
                .forResult(PictureConfig.CHOOSE_REQUEST)
            modePictureSelector=0
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            if(modePictureSelector==1){
            Account.userPhoto(selectList[0].path.toUri())
            Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding.userPhotoAboutMe)
            }
            else
            {
                Account.userBackGround(selectList[0].path.toUri())
                Glide.with(AllApplication.context).load(Account.userBackGround()).into(binding.userPhotoBGAboutMe)
            }
        }
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