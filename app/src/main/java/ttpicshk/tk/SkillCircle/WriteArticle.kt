package ttpicshk.tk.SkillCircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
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
            it.title="发表文章"
            it.subtitle=Account.userName()
        }


        binding.selectPhotoWriteArticle.setOnClickListener {
            selectPhoto()
        }
        binding.atWriteArticle.setOnClickListener {
            "at".showToast(this)
        }
        binding.sharpWriteArticle.setOnClickListener {
            "sharp".showToast(this)
        }
        binding.sendWriteArticle.setOnClickListener {
            sendArticle()
        }
    }

    private fun sendArticle() {
        "send success".showToast(AllApplication.context)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            val bindingList= listOf<ImageView>(binding.photo1WriteArticle,binding.photo2WriteArticle,
                binding.photo3WriteArticle,binding.photo4WriteArticle,binding.photo5WriteArticle,
            binding.photo6WriteArticle,binding.photo7WriteArticle,binding.photo8WriteArticle,
            binding.photo9WriteArticle)
            val bindingLists= listOf<View>(binding.photoG1,binding.photoG2,binding.photoG3)
            runOnUiThread {
                for((i, item) in selectList.withIndex()) {
                    Glide.with(AllApplication.context).load(item.path)
                        .into(bindingList[i])
                    if(i%3==0){
                        bindingLists[i/3].visibility=View.VISIBLE
                    }
                    if(i==2)
                        binding.photoG3s.visibility=View.VISIBLE
                }
            }

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