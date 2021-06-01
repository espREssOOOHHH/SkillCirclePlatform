package ttpicshk.tk.SkillCircle

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticleAdapter(val context:Context,val articleList:List<Article>)
    :RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

        inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
            val title:TextView=view.findViewById(R.id.article_Title)
            val image:ImageView=view.findViewById(R.id.article_Image)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(AllApplication.context).inflate(R.layout.article_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            val article=articleList[position]
            val intent= Intent(context,ArticleDetail::class.java).apply {
                putExtra(ArticleDetail.ARTICLE_TITLE,article.title)
                putExtra(ArticleDetail.ARTICLE_IMAGE_ID,article.imageId)
                putExtra(ArticleDetail.ARTICLE_CONTENT,article.content)
            }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article=articleList[position]
        holder.title.text=article.title
        Glide.with(context).load(article.imageId).into(holder.image)
    }

    override fun getItemCount(): Int =articleList.size
}