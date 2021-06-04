package ttpicshk.tk.SkillCircle

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.pictureselector.GlideEngine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import ttpicshk.tk.SkillCircle.AllApplication.Companion.context
import ttpicshk.tk.SkillCircle.databinding.AboutMeAccountSettingsBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsMoreBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsPersonalInfoBinding
import java.io.IOException
import java.util.concurrent.TimeUnit
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
        else{
            Loading.start(this)
            load()
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

        refreshPersonalInfo()
        bindingAccountSettings.aboutMePasswordSettingBtn.setOnClickListener{
            "设置密码".showToast(AllApplication.context)
            refreshPersonalInfo()
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
            changePersonalInfo(1,this)
        }
        bindingPersonalInfo.aboutMeLocationBtn.setOnClickListener {
            changePersonalInfo(2,this)
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
            changePersonalInfo(3,this)
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

    private fun changePersonalInfo(type:Int,context: Context) {
        val dialog= Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_change_personal_info_1)
        val title=dialog.findViewById<TextView>(R.id.title_dialog_personalInfo1)
        val text=dialog.findViewById<EditText>(R.id.text_dialog_personalInfo1)
        val commit=dialog.findViewById<Button>(R.id.submit_dialog_personalInfo1)
        val cancel=dialog.findViewById<Button>(R.id.cancel_dialog_personalInfo1)

        when(type){//1:userName 2:location 3:signature
            1->{
                title.text="一个新的昵称哟"
            }
            2->{
                title.text="经常在哪里🗺出没？"
            }
            3->{
                title.text="🖊新的签名哟"
            }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        commit.setOnClickListener {
            Loading.start(this)
            commitPersonalInfo(type,text.text.toString(),context)
        }
        dialog.show()

    }
    private fun commitPersonalInfo(type:Int,data:String,context: Context){
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val bodyT= MultipartBody.Builder().setType(MultipartBody.FORM)
            val body:RequestBody =
                when(type){
                1->{
                    bodyT.addFormDataPart("name",data)
                        .build()
                }
                2->{
                    bodyT.addFormDataPart("location",data)
                        .build()
                }
                3->{
                    bodyT.addFormDataPart("signature",data)
                        .build()
                }
                4->{
                    bodyT.addFormDataPart("birthday",data)
                        .build()
                }
                else->{
                    bodyT.build()
                }
            }
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/edituserinfo")
                .method("POST", body)
                .addHeader("token",Account.token)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        "提交失败！请重试".showToast(AllApplication.context)
                        Loading.stop()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        "提交成功！".showToast(context)
                        Loading.stop()
                        refreshPersonalInfo()
                    }
                }
            })
        }
    }

    private fun refreshPersonalInfo() {
        binding.collapseToolBarAboutMe.title=Account.userName()
        binding.subtitleAboutMe.text="id:"+Account.id().toString()
        bindingPersonalInfo.aboutMeUsername.text=Account.userName()
        bindingPersonalInfo.aboutMeSignature.text=Account.signature()
        bindingPersonalInfo.aboutMeBirthday.text=Account.birthday()
        bindingPersonalInfo.aboutMeGender.text=Account.gender()
        bindingPersonalInfo.aboutMeLocation.text=Account.location()
        bindingAccountSettings.aboutMeEmail.text=Account.email()
        bindingAccountSettings.aboutMePhone.text=Account.phone()
    }

    inner class dataJson{
        var msg:String=""
        var data=Data()
        inner class Data{
            lateinit var list:List<list>
        }
        inner class list{
            val id = 0
            val user_id = 0
            val age = 0
            val sex = 0
            val qg = 0//情感状况
            val job: String?=null
            val path: String=""//住址
            val birthday: String=""
            val signature:String=""
            var nickname:String? = null
        }

    }
    fun load(){
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/getuserinfo")
                //.url("http://ceshi.299597.xyz/api/v1/post/339")
                .method("GET",null)
                .addHeader("token",Account.token)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Loading.stop()
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, dataJson::class.java)
                    Loading.stop()
                    Account.setPersonalInfo(message.data.list[0].id,message.data.list[0].user_id,
                        message.data.list[0].nickname,message.data.list[0].age,
                    message.data.list[0].sex,message.data.list[0].qg,message.data.list[0].job,
                        message.data.list[0].path,message.data.list[0].birthday,message.data.list[0].signature)
                    runOnUiThread { refreshPersonalInfo() }
                }
            })
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