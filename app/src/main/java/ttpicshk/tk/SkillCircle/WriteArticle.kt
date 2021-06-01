package ttpicshk.tk.SkillCircle

import android.content.Intent
import android.graphics.Picture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.pictureselector.GlideEngine
import ttpicshk.tk.SkillCircle.databinding.WriteArticleLayoutBinding

class WriteArticle : AppCompatActivity() {
    lateinit var binding: WriteArticleLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= WriteArticleLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //codes about action Bar
        setSupportActionBar(binding.toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.icon_close)
        }


        //select photo
        /*val resultLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
                result->
                if (result.resultCode == RESULT_OK) {
                    val data=result.data
                    var selectList = PictureSelector.obtainMultipleResult(data)
                    Glide.with(AllApplication.context).load(selectList[0].path)
                        .into(binding.photo1WriteArticle)
                }
        }*/
        binding.selectPhotoWriteArticle.setOnClickListener {
            selectPhoto()
            //resultLauncher.launch(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val data=data
            var selectList = PictureSelector.obtainMultipleResult(data)
            Glide.with(AllApplication.context).load(selectList[0].path)
                .into(binding.photo1WriteArticle)
        }
    }


    fun selectPhoto(){
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .theme(R.style.Theme_SkillCirclePlatform)
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(9)
            .minSelectNum(1)
            .forResult(PictureConfig.CHOOSE_REQUEST)

    }
}