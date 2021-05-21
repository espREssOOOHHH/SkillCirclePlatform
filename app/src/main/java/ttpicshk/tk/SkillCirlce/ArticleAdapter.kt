package ttpicshk.tk.SkillCirlce

import android.content.Context
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
        val view=LayoutInflater.from(context).inflate(R.layout.article_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article=articleList[position]
        holder.title.text=article.title
        Glide.with(context).load(article.imageId).into(holder.image)
    }

    override fun getItemCount(): Int =articleList.size
}