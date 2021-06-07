package ttpicshk.tk.SkillCircle.Frags

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Path
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.config.IndicatorConfig
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import okhttp3.*
import ttpicshk.tk.SkillCircle.*
import ttpicshk.tk.SkillCircle.databinding.FragHomepageBinding
import java.io.IOException
import kotlin.concurrent.thread

class Frags_1_homePage(var idFrag:Int):Fragment() {

    inner class messageJson(){
        var msg:String=""
        var errorCode=0
        var data=Data()
        //lateinit var data:List<list>
        inner class Data{
            lateinit var list:List<list>
        }
        inner class list{
            var id = 0
            var title=""
            var titlepic=""
            var desc=""
            var type=0
            var create_time=""
            var topic_class_id=0
        }
    }

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


            getBannerData()

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

    val handler=object:Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val data: messageJson = msg.obj as messageJson
            when (msg.arg1) {
                1->{//homepage title picture
                    if(msg.arg2==1){
                        val listData=ArrayList<String>()
                        data.data.list.forEach {
                            listData.add(it.titlepic)
                        }
                        useBanner(listData)
                    }else{
                        "首页图片加载失败！".showToast(AllApplication.context)
                    } }
            }
        }
    }

    private fun getBannerData(){
        thread {
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("https://ceshi.299597.xyz/api/v1/hottopic")
                .method("GET", null)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg= Message()
                    msg.arg1=1
                    msg.arg2=0
                    msg.obj=messageJson()
                    handler.sendMessage(msg)
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val message = gson.fromJson(data, messageJson::class.java)
                    val msg= Message()
                    msg.arg1=1
                    msg.arg2=1
                    msg.obj=message
                    handler.sendMessage(msg)
                }
            })
        }
    }

    private fun useBanner(imageUrls:List<String>) {
       /* val imageUrls=listOf("https://tangzhe123-com.oss-cn-shenzhen.aliyuncs.com/Appstatic/qsbk/demo/topicpic/1.jpeg",
            "https://tangzhe123-com.oss-cn-shenzhen.aliyuncs.com/Appstatic/qsbk/demo/topicpic/2.jpeg"
        )*/
        Log.d("login_network",imageUrls.size.toString())
        binding.banner.visibility=View.VISIBLE
        binding.banner.addBannerLifecycleObserver(activity)
            .setIndicator(CircleIndicator(AllApplication.context))
            .setScrollTime(1000)
            .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
            .setAdapter(object : BannerImageAdapter<String>(imageUrls) {
            override fun onBindView(holder: BannerImageHolder, data: String, position: Int, size: Int) {
                Glide.with(AllApplication.context)
                    .load(data)
                    .into(holder.imageView)
            }
        })
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