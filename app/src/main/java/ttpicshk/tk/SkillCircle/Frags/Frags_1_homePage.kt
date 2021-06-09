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
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*
import kotlin.collections.ArrayList
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
    inner class messageJsonS(){
        var msg:String=""
        var data=Data()
        inner class Data{
            lateinit var list:List<Lists>
        }
        inner class Lists{
            var id=0
            var user_id=0
            val title=""
            var titlepic=""
            var content=""
            var sharenum=0
            var path=""
            var type=0
            var create_time=""
            var post_class_id=0
            var share_id=0
            var isopen=0
            var user=User()
            var images=ArrayList<Images>()
            //var share=""
            var pivot=Pivot()
        }
        inner class Pivot{
            var id=0
            var topic_id=0
            var post_id=0
            var create_time=0
        }
        inner class Images{
            var url=""
            var pivot=Pivot()
        }
        inner class User{
            var id=0
            var username=""
            var userpic=""
        }
    }
    public inline fun messageJsonS.Lists.imageToArray(): ArrayList<String> {
        var l=ArrayList<String>(0)
        this.images.forEach {
            l.add(it.url)
        }
        if(this.images.size==0){
            var p= ArrayList<String>(0)
            return p
        }
        return l
    }

    private lateinit var viewModel:MainViewModel
   /* val article= mutableListOf(Article("Grape-Title","grapes".repeat(10),
        arrayListOf("https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7"),
        "2020-12-20","https://tse4-mm.cn.bing.net/th/id/OIP.gH3rAGvlRDPBfEtlbRHVzgHaEo?w=234&h=180&c=7&o=5&dpr=2&pid=1.7"
    ))*/
    lateinit var binding:FragHomepageBinding
    lateinit var adapter: ArticleAdapter
    @SuppressLint("ResourceAsColor")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel= ViewModelProvider(this)[MainViewModel::class.java]
        binding= FragHomepageBinding.inflate(layoutInflater)


        binding.recyclerViewFrags.layoutManager=GridLayoutManager(this.activity,1)
        adapter=ArticleAdapter(AllApplication.context,viewModel.articleList1)
        binding.recyclerViewFrags.adapter=adapter
        initArticle()

        binding.swipeRefreshFrag.setColorSchemeColors(R.color.design_default_color_primary_dark)
        binding.swipeRefreshFrag.setOnRefreshListener {
            refreshArticle(adapter)
        }

        //拉到最下面刷新
        binding.recyclerViewFrags.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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
            when (msg.arg1) {
                1->{//homepage title picture
                    if(msg.arg2==1){
                        val data: messageJson = msg.obj as messageJson
                        val listData=ArrayList<String>()
                        data.data.list.forEach {
                            listData.add(it.titlepic)
                        }
                        useBanner(listData)
                    }else{
                        "首页图片加载失败！".showToast(AllApplication.context)
                    } }
                2->{//article
                    if(msg.arg2==1){
                        val data:messageJsonS=msg.obj as messageJsonS
                        val listData=ArrayList<Article>()
                        data.data.list.forEach {
                            listData.add(Article(it.title,it.content,it.imageToArray(),it.create_time,
                            it.titlepic,it.user.username))
                        }
                        viewModel.articleList1.clear()
                        listData.forEach{
                            viewModel.articleList1.add(it)
                        }
                        adapter.notifyDataSetChanged()
                    }else{
                        "首页加载失败，请重试！".showToast(AllApplication.context)
                    }
                }
            }
        }
    }

    private fun getBannerData(){
        getData("https://ceshi.299597.xyz/api/v1/hottopic",1)
    }
    fun getArticle(){
        getData("https://ceshi.299597.xyz/api/v1/topic/${idFrag+1}/post/1",2)
    }
    private fun getData(URL:String,type:Int){
        thread {
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url(URL)
                .method("GET", null)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg= Message()
                    if(type==1){
                    msg.arg1=1
                    msg.arg2=0
                    msg.obj=messageJson()}
                    else{
                        msg.arg1=2
                        msg.arg2=0
                        msg.obj=messageJsonS()
                    }
                    handler.sendMessage(msg)
                }

                override fun onResponse(call: Call, response: Response) {
                    val data = response.body!!.string()
                    Log.d("login_network", data)
                    val gson = Gson()
                    val msg = Message()
                    var message:Any
                    if(type==1) {
                        message = gson.fromJson(data, messageJson::class.java)
                        msg.arg1 = 1
                        msg.arg2 = 1
                    }else{
                        message=gson.fromJson(data,messageJsonS::class.java)
                        msg.arg1=2
                        msg.arg2=1
                    }
                    msg.obj=message
                    handler.sendMessage(msg)
                }
            })
        }
    }
    private fun useBanner(imageUrls:List<String>) {
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
        getArticle()
        getBannerData()
    }
}