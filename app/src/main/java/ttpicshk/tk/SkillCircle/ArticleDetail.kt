package ttpicshk.tk.SkillCircle

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCircle.databinding.ArticleDetailBinding

class ArticleDetail : AppCompatActivity() {
    lateinit var binding:ArticleDetailBinding

    companion object{
        const val ARTICLE_TITLE="article_title"
        const val ARTICLE_IMAGE_ID="article_image_id"
        const val ARTICLE_CONTENT="article_content"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title=intent.getStringExtra(ARTICLE_TITLE)?:"文章详情"
        val imageId=intent.getSerializableExtra("IMAGES") as ArrayList<*>
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBar.title=title

        if(!(imageId).isNullOrEmpty()){
            Glide.with(AllApplication.context).load(Uri.parse(imageId[0] as String)).into(binding.headImageView)
        }


        val text=intent.getStringExtra(ARTICLE_CONTENT)?:"文章文章文章\n文章文章文章\n"
        binding.articleText.text=text
        binding.articleComment.text=title.repeat(50)

        Glide.with(AllApplication.context).load(Account.userPhoto()).into(binding.userPhotoArticleDetail)

        binding.sendCommentButtonArticleDetail.setOnClickListener {
            if(!Account.IsOnLine()){
                startActivity(Intent(AllApplication.context,LogInActivity::class.java))
            }
                else{
            binding.articleComment.text=binding.commentArticleDetail.text
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
            R.id.more->{
                "more".showToast(AllApplication.context)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_article_detail,menu)
        return true
    }

}