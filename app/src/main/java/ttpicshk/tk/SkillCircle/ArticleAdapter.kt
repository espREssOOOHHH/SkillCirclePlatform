package ttpicshk.tk.SkillCircle

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArticleAdapter(val context:Context, private val articleList:ArrayList<Article>)
    :RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

        inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
            val title:TextView=view.findViewById(R.id.article_Title)
            val image_1:ImageView=view.findViewById(R.id.article_Image_1)
            val create_time:TextView=view.findViewById(R.id.article_time)
            val author_photo:de.hdodenhof.circleimageview.CircleImageView=view.findViewById(R.id.article_author_photo)
            val author:TextView=view.findViewById(R.id.article_author_name)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(AllApplication.context).inflate(R.layout.article_item,parent,false)
        val holder=ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position=holder.adapterPosition
            val article=articleList[position]
            val intent= Intent(context,ArticleDetail::class.java).apply {
                putExtra(ArticleDetail.ARTICLE_TITLE,article.title)
                putExtra("IMAGES",article.images)
                putExtra(ArticleDetail.ARTICLE_CONTENT,article.content)
            }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article=articleList[position]
        holder.title.text=article.title
        holder.create_time.text=article.create_time
        holder.author.text=article.authorname
        if(article.headImage.length>1){
            Glide.with(AllApplication.context).load(Uri.parse(article.headImage)).into(holder.author_photo)}

        if(article.images.size==1) {
            holder.image_1.visibility=View.VISIBLE
            Glide.with(AllApplication.context).load(Uri.parse(article.images[0])).into(holder.image_1)
        }
    }

    override fun getItemCount(): Int =articleList.size
}