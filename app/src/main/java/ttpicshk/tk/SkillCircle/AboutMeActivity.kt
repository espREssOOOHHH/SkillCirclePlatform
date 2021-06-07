package ttpicshk.tk.SkillCircle

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.pictureselector.GlideEngine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ttpicshk.tk.SkillCircle.databinding.AboutMeAccountSettingsBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeLayoutBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsMoreBinding
import ttpicshk.tk.SkillCircle.databinding.AboutMeSettingsPersonalInfoBinding
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class AboutMeActivity : AppCompatActivity() {
    lateinit var binding: AboutMeLayoutBinding
    lateinit var bindingPersonalInfo: AboutMeSettingsPersonalInfoBinding
    lateinit var bindingAccountSettings: AboutMeAccountSettingsBinding
    lateinit var bindingSettingsMore: AboutMeSettingsMoreBinding

    var modePictureSelector = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutMeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingPersonalInfo = binding.aboutMeSettingsPersonalInfo
        bindingAccountSettings = binding.aboutMeAccountSettings
        bindingSettingsMore = binding.aboutMeSettingsMore

        if (!Account.IsOnLine()) {
            "请先登录".showToast(this)
            finish()
            startActivity(Intent(this, LogInActivity::class.java))
        } else {
            load(this)
            loadImage()
        }


        setSupportActionBar(binding.toolbarAboutMe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBarAboutMe.title = Account.userName()
        binding.userPhotoAboutMe.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.photo_big_view)
            val picture = dialog.findViewById<ImageView>(R.id.pictureViewDialog)
            Glide.with(this).load(Account.userPhoto()).into(picture)
            dialog.show()
        }
        binding.userPhotoBGAboutMe.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.photo_big_view)
            val picture = dialog.findViewById<ImageView>(R.id.pictureViewDialog)
            Glide.with(this).load(Account.userBackGround()).into(picture)
            dialog.show()
        }


        bindingAccountSettings.aboutMePasswordSettingBtn.setOnClickListener {
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
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://baidu.com")
            startActivity(intent)
        }
        bindingPersonalInfo.aboutMeUsernameBtn.setOnClickListener {
            changePersonalInfo(1, this)
        }
        bindingPersonalInfo.aboutMeLocationBtn.setOnClickListener {
            changePersonalInfo(2, this)
        }
        bindingPersonalInfo.aboutMeBirthdayBtn.setOnClickListener {
            changePersonalInfo(4,this)

        }
        bindingPersonalInfo.aboutMeGenderBtn.setOnClickListener {
            changePersonalInfo(5,this)
        }
        bindingPersonalInfo.aboutMeSignatureBtn.setOnClickListener {
            changePersonalInfo(3, this)
        }
        bindingPersonalInfo.aboutMeUserPhotoBtn.setOnClickListener {
            modePictureSelector = 1
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
            modePictureSelector = 0
        }
    }

    private fun changePersonalInfo(type: Int, context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_change_personal_info_1)
        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker_dialog_personalInfo1)
        val title = dialog.findViewById<TextView>(R.id.title_dialog_personalInfo1)
        val text = dialog.findViewById<EditText>(R.id.text_dialog_personalInfo1)
        val commit = dialog.findViewById<Button>(R.id.submit_dialog_personalInfo1)
        val cancel = dialog.findViewById<Button>(R.id.cancel_dialog_personalInfo1)
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->}
        val spinner=dialog.findViewById<Spinner>(R.id.genderSelect_dialog_personalInfo1)
        var gender=""
        when (type) {//1:userName 2:location 3:signature 4:birthday 5:性别
            1 -> {
                text.visibility = View.VISIBLE
                title.text = "一个新的昵称哟"
            }
            2 -> {
                text.visibility = View.VISIBLE
                title.text = "经常在哪里🗺出没？"
            }
            3 -> {
                text.visibility = View.VISIBLE
                title.text = "🖊新的签名哟"
            }
            4 -> {
                datePicker.visibility = View.VISIBLE
                title.text = "在什么时候🎂出生呢？"
            }
            5->{
                spinner.visibility=View.VISIBLE
                val spinnerOption=arrayOf("🚹男","🚺女","🈲保密")
                spinner.adapter=ArrayAdapter(context,
                    android.R.layout.simple_spinner_dropdown_item,spinnerOption)
                spinner.onItemSelectedListener=object : AdapterView.OnItemClickListener,
                    AdapterView.OnItemSelectedListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        gender=position.toString()
                    }
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        gender=position.toString()
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?){}
                }
            }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        commit.setOnClickListener {
            when (type) {
                4 -> {
                    val selectDate:String="${datePicker.year}-${datePicker.month+1}-${datePicker.dayOfMonth}"
                    Loading.start(this)
                    commitPersonalInfo(type, selectDate, context, dialog)
                }
                5->{
                    Loading.start(this)
                    commitPersonalInfo(type, gender, context, dialog)
                }
                else -> {
                    Loading.start(this)
                    commitPersonalInfo(type, text.text.toString(), context, dialog)
                }
            }
        }
        dialog.show()
    }
    private fun commitPersonalInfo(type: Int, data: String, context: Context, dialog: Dialog){
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val bodyT= MultipartBody.Builder().setType(MultipartBody.FORM)
            val body:RequestBody =
                when(type){
                1->{//1:userName 2:location 3:signature 4:birthday
                    bodyT.addFormDataPart("nickname",data)
                        .addFormDataPart("name",data)
                        .addFormDataPart("signature",Account.signature())
                        .addFormDataPart("birthday",Account.birthday())
                        .addFormDataPart("path",Account.location())
                        .addFormDataPart("age",Account.age().toString())
                        .addFormDataPart("qg",Account.EmotionalState().toString())
                        .addFormDataPart("sex", Account.genderDigit().toString())
                        .addFormDataPart("job",Account.occupation())
                        .build()
                }
                2->{
                    bodyT.addFormDataPart("path",data)
                        .addFormDataPart("nickname",Account.userName())
                        .addFormDataPart("name",Account.userName())
                        .addFormDataPart("signature",Account.signature())
                        .addFormDataPart("birthday",Account.birthday())
                        .addFormDataPart("age",Account.age().toString())
                        .addFormDataPart("qg",Account.EmotionalState().toString())
                        .addFormDataPart("sex", Account.genderDigit().toString())
                        .addFormDataPart("job",Account.occupation())
                        .build()
                }
                3->{
                    bodyT.addFormDataPart("signature",data)
                        .addFormDataPart("path",Account.location())
                        .addFormDataPart("nickname",Account.userName())
                        .addFormDataPart("name",Account.userName())
                        .addFormDataPart("birthday",Account.birthday())
                        .addFormDataPart("age",Account.age().toString())
                        .addFormDataPart("qg",Account.EmotionalState().toString())
                        .addFormDataPart("sex", Account.genderDigit().toString())
                        .addFormDataPart("job",Account.occupation())
                        .build()
                }
                4->{
                    bodyT.addFormDataPart("birthday",data)
                        .addFormDataPart("path",Account.location())
                        .addFormDataPart("nickname",Account.userName())
                        .addFormDataPart("name",Account.userName())
                        .addFormDataPart("signature",Account.signature())
                        .addFormDataPart("age",Account.age().toString())
                        .addFormDataPart("qg",Account.EmotionalState().toString())
                        .addFormDataPart("sex", Account.genderDigit().toString())
                        .addFormDataPart("job",Account.occupation())
                        .build()
                }
                    5->{
                        bodyT.addFormDataPart("birthday",Account.birthday())
                            .addFormDataPart("path",Account.location())
                            .addFormDataPart("nickname",Account.userName())
                            .addFormDataPart("name",Account.userName())
                            .addFormDataPart("signature",Account.signature())
                            .addFormDataPart("age",Account.age().toString())
                            .addFormDataPart("qg",Account.EmotionalState().toString())
                            .addFormDataPart("sex", data)
                            .addFormDataPart("job",Account.occupation())
                            .build()
                    }
                else->{
                    bodyT.build()
                }
            }
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/edituserinfo")
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
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, dataJsonS::class.java)
                    runOnUiThread {
                        if(message.errorCode!=0){
                            "修改错误！错误原因是：${message.msg}".showToast(context)
                        }else{
                            "提交成功！".showToast(context)
                        }
                        dialog.dismiss()
                        Loading.stop()
                        if((message.errorCode==0) or (message.msg == "修改成功"))
                            load(context)
                    }
                }
            })
        }
    }

    //不一定能用
    private fun commitPhoto(type:Int,context: Context) {
        thread {//type=1:头像 2:背景
            val photo: Uri = when (type) {
                1 -> Account.userPhoto()
                else -> Account.userBackGround()
            }
            val website: String = when (type) {
                1 -> "https://ceshi.299597.xyz/api/v1/edituserpic"
                else -> "https://ceshi.299597.xyz/api/v1/editbackground"
            }
            val name:String=when(type){
                1->"userpic"
                else->"background"
            }
            val picture=UriToFile.getPath(context,photo)
            Log.d("login_network", "$picture")
            val client = OkHttpClient().newBuilder().connectTimeout(3, TimeUnit.SECONDS)
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    name,
                  picture!!.substring(photo.path!!.lastIndexOf("/") + 1, picture.length),
                    File(picture)
                        .asRequestBody("application/octet-stream".toMediaTypeOrNull())
                )
                .build()
            val request: Request = Request.Builder()
                .url(website)
                .method("POST", body)
                .addHeader("token", Account.token)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        "提交失败！请重试".showToast(AllApplication.context)
                        Loading.stop()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, dataJsonS::class.java)
                    runOnUiThread {
                        if (message.errorCode != 0) {
                            "修改错误！错误原因是：${message.msg}".showToast(context)
                        } else
                            load(context)
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
        Glide.with(AllApplication.context).load(Account.userBackGround()).into(binding.userPhotoBGAboutMe)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding.userPhotoAboutMe)
    }


    inner class dataJson{
        var msg:String=""
        var data=Data()
        var errorCode=0
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
            var background:String=""
        }
    }
    inner class dataJsonS{
        var msg:String=""
        var errorCode=0
        lateinit var data:List<list>
        inner class list{
            var id = 0
            var user_id = 0
            var age = 0
            var sex = 0
            var qg = 0//情感状况
            var job: String?=null
            var path: String=""//住址
            var birthday: String=""
            var signature:String=""
            var nickname:String? = null
        }
    }
    inner class dataJsonP{
        var msg:String=""
        var errorCode=0
        lateinit var data:Data
        inner class Data{
            lateinit var list:List
        }
        inner class List{
            var userpic=""
            var background=""
        }
    }
    fun load(context: Context,refreshDisplay:Boolean=true){
        if(refreshDisplay){ Loading.start(context)}
        thread {
            Log.d("login_network", "start")
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/getuserinfo")
                .method("GET",null)
                .addHeader("token",Account.token)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if(refreshDisplay){Loading.stop()}
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, dataJson::class.java)
                    if(refreshDisplay){Loading.stop()}
                    Account.setPersonalInfo(message.data.list[0].id,message.data.list[0].user_id,
                        message.data.list[0].nickname,message.data.list[0].age,
                    message.data.list[0].sex,message.data.list[0].qg,message.data.list[0].job,
                        message.data.list[0].path,message.data.list[0].birthday,message.data.list[0].signature)
                    Account.setPhotos(null,message.data.list[0].background)
                    if(refreshDisplay){runOnUiThread { refreshPersonalInfo() }}
                }
            })
        }
    }

    fun loadImage(){
        thread {

            Log.d("login_network", "startLoadingImage")
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/user/getpic")
                .method("GET",null)
                .addHeader("token",Account.token)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()

                    val gson = Gson()
                    val message = gson.fromJson(data, dataJsonP::class.java)

                   Account.setPhotos(message.data.list.userpic,message.data.list.background)

                   runOnUiThread {
                       if (message.errorCode != 0) {
                           "上传错误！错误原因：${message.msg}".showToast(AllApplication.context)
                       } else {
                           refreshPersonalInfo()
                       }
                   }
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
                commitPhoto(1,this)
            }
            else
            {
                commitPhoto(2,this)
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
