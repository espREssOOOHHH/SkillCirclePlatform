package ttpicshk.tk.SkillCircle.Frags

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ttpicshk.tk.SkillCircle.*
import ttpicshk.tk.SkillCircle.databinding.FragHomepageBinding
import kotlin.concurrent.thread

class Frags_1_homePage(var idFrag:Int):Fragment() {

    private lateinit var viewModel:MainViewModel
    val article= mutableListOf(Article("Grape-Title","grapes".repeat(10),
        arrayListOf(Uri.parse("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7"))
    ),
    Article("Duck-Title","ducks".repeat(10), arrayListOf(Uri.parse("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7"))),
    Article("Book","books".repeat(10), arrayListOf(Uri.parse("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7")))
    )
    lateinit var binding:FragHomepageBinding
    @SuppressLint("ResourceAsColor")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel= ViewModelProvider(this)[MainViewModel::class.java]
        binding= FragHomepageBinding.inflate(layoutInflater)

        initArticle()

        binding.recyclerViewFrags.layoutManager=GridLayoutManager(this.activity,1)
        val adapter=ArticleAdapter(AllApplication.context,viewModel.articleList1)
        binding.recyclerViewFrags.adapter=adapter

        binding.swipeRefreshFrag.setColorSchemeColors(R.color.design_default_color_primary_dark)
        binding.swipeRefreshFrag.setOnRefreshListener {
            refreshArticle(adapter)
        }

        //下滑按钮
        binding.fabFrags.setOnClickListener {
            binding.nestScrollViewFrags.smoothScrollTo(0,0)
        }

        binding.nestScrollViewFrags.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener
            { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if(scrollY>oldScrollY){//down
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.fabFrags.visibility=View.GONE
                    },2000)
                }
                if(scrollY<oldScrollY){//up
                    binding.fabFrags.visibility=View.VISIBLE
                }
                else
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.fabFrags.visibility=View.GONE
                    },2000)

            })

        return binding.root
    }

    private fun refreshArticle(adapter: ArticleAdapter){
        thread {
            Thread.sleep(1000)
            requireActivity().runOnUiThread{
                Snackbar.make(binding.nestScrollViewFrags,"Refreshed",Snackbar.LENGTH_SHORT)
                    .setAction("Got it"){}
                    .show()
                initArticle()
                adapter.notifyDataSetChanged()
                binding.swipeRefreshFrag.isRefreshing=false
            }
        }
    }

    private fun initArticle(){
        viewModel.articleList1.clear()
        val i=(10..20).random()
        repeat(i){
            val index=(0 until article.size).random()
            viewModel.articleList1.add(article[index])
        }
    }
}