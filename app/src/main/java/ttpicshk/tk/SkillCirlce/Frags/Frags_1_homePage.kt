package ttpicshk.tk.SkillCirlce.Frags

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import ttpicshk.tk.SkillCirlce.AllApplication
import ttpicshk.tk.SkillCirlce.Article
import ttpicshk.tk.SkillCirlce.ArticleAdapter
import ttpicshk.tk.SkillCirlce.R

class Frags_1_homePage:Fragment() {

    val article= mutableListOf(Article("fudk","skdfjshfk",R.drawable.nav_location),
    Article("dskjfh","sdkfjhs",R.drawable.ic_backup)
    )
    val articleList=ArrayList<Article>()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var fragmentView = inflater.inflate(R.layout.frag_homepage, container, false)
        initArticle()
        val recyclerView=fragmentView.findViewById<RecyclerView>(R.id.recyclerViewFrags)
        recyclerView.layoutManager=GridLayoutManager(this.activity,2)
        val adapter=ArticleAdapter(AllApplication.context,articleList)
        recyclerView.adapter=adapter

        val swipeRefresh=fragmentView.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshFrag)
        swipeRefresh?.setColorSchemeColors(R.color.design_default_color_primary_dark)
        swipeRefresh?.setOnRefreshListener {
            Snackbar.make(fragmentView,"Refreshed",Snackbar.LENGTH_SHORT)
                .setAction("Got it"){}
                .show()
        }
        return fragmentView
    }

    private fun initArticle(){
        articleList.clear()
        repeat(10){
            val index=(0 until article.size).random()
            articleList.add(article[index])
        }
    }
}