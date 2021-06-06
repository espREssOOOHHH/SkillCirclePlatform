package ttpicshk.tk.SkillCircle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import okhttp3.*
import ttpicshk.tk.SkillCircle.Frags.Frags_1_homePage
import ttpicshk.tk.SkillCircle.Frags.Frags_2_homePage
import ttpicshk.tk.SkillCircle.databinding.HomePageBinding
import java.io.IOException
import kotlin.concurrent.thread
import kotlin.system.exitProcess


class HomePage:AppCompatActivity() {

    inner class messageJson(){
        var msg:String=""
        var errorCode=0
        var data=Data()
        //lateinit var data:List<list>
        inner class Data{
            lateinit var list:List<list>
        }
        inner class list{
            val id = 0
            val classname=""
        }
    }

    lateinit var binding:HomePageBinding
    lateinit var viewModel: MainViewModel
    private var firstQuitTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(AppObserver(lifecycle))
        viewModel=ViewModelProvider(this)[MainViewModel::class.java]



        //codes about action Bar
        setSupportActionBar(binding.toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        //codes about menu
        binding.navView.setCheckedItem(R.id.nav_homePage)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_aboutMe->startActivity(Intent(this,AboutMeActivity::class.java))
                R.id.nav_settings->startActivity(Intent(this,SettingPage::class.java))

            }
            binding.drawerLayout.closeDrawers()
            true
        }

        getPagerTitle(this)
    }

    val handler=object:Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val data: messageJson = msg.obj as messageJson
            when (msg.arg1) {
                1->{//首页拉取title
                    if((msg.arg2==0) or (data.errorCode!=0))
                    {
                        "拉取首页失败！".showToast(AllApplication.context)
                    }
                    else{
                        loadTitle(data)
                    }
                }
            }
        }
    }

   private fun getPagerTitle(context: Context) {
       thread {
           val client = OkHttpClient().newBuilder()
               .build()
           val request: Request = Request.Builder()
               .url("https://ceshi.299597.xyz/api/v1/postclass")
               .method("GET", null)
               .build()

           client.newCall(request).enqueue(object : Callback {
               override fun onFailure(call: Call, e: IOException) {
                   val msg=Message()
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
                   val msg=Message()
                   msg.arg1=1
                   msg.arg2=1
                   msg.obj=message
                   handler.sendMessage(msg)
               }
           })
       }
    }

    private fun loadTitle(jsonData:messageJson){
        binding.homePage1.visibility=View.INVISIBLE
        binding.homePage2.visibility=View.VISIBLE
        //fragmentManager and TabLayout
        val adapterFrag=Adapter_frags_homePage(supportFragmentManager,lifecycle)
        repeat(jsonData.data.list.size) {
            adapterFrag.addFrag(Frags_1_homePage("$it".toInt()))
        }
        Log.d("login_network",jsonData.msg)
        repeat(0){
            adapterFrag.addFrag(Frags_2_homePage())
        }

        binding.pagerLayout.adapter=adapterFrag
        TabLayoutMediator(binding.tabLayout,binding.pagerLayout){tab,position->
            tab.text= jsonData.data.list[position].classname
            tab.setIcon(R.drawable.icon_model)
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                recoverItemTab()
                chooseTab(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun chooseTab(tab: TabLayout.Tab?) {}
    private fun recoverItemTab() {}
    override fun onResume() {
        invalidateOptionsMenu()
        binding.navView.setCheckedItem(R.id.nav_homePage)
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val iconDrawer=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.menu_icon)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(iconDrawer)
        val background=findViewById<ImageView>(R.id.menu_background)
        Glide.with(AllApplication.context).load(Account.userBackGround()).into(background)
        val name=findViewById<TextView>(R.id.menu_userText)
        name.text=Account.userName()
        val signature=findViewById<TextView>(R.id.menu_signature)
        signature.text=Account.signature()
        return super.onPrepareOptionsMenu(menu)
    }

    //按两下back退出
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK ){
            if(System.currentTimeMillis()-firstQuitTime>2000){
                "Touch Back Button Again to Quit".showToast(AllApplication.context)
                firstQuitTime=System.currentTimeMillis()
            }else{
                finish()
                exitProcess(0)
            }
        }
        return true
    }

    //codes about menu button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        val iconDrawer=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.menu_icon)
        val background=findViewById<ImageView>(R.id.menu_background)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(iconDrawer)
        Glide.with(AllApplication.context).load(Account.userBackGround()).into(background)
        val name=findViewById<TextView>(R.id.menu_userText)
        name.text=Account.userName()
        val signature=findViewById<TextView>(R.id.menu_signature)
        signature.text=Account.signature()

        //搜索框
        val search:MenuItem=menu!!.findItem(R.id.search_actionBar)
        val searchView:SearchView= search.actionView as SearchView
        searchView.isIconifiedByDefault=true
        searchView.isSubmitButtonEnabled=true
        searchView.isSubmitButtonEnabled=true
        searchView.imeOptions=EditorInfo.IME_ACTION_SEARCH
        searchView.isIconified=false
        searchView.isFocusable=true
        searchView.requestFocusFromTouch()
        searchView.queryHint="请输入内容"
        //search on action bar
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Snackbar.make(binding.pagerLayout,"🔍: $query",Snackbar.LENGTH_SHORT).show()
                searchView.clearFocus()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        iconDrawer.setOnClickListener {
            startActivity(Intent(this,AboutMeActivity::class.java))
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.write_article_actionBar-> {
                startActivity(Intent(this,WriteArticle::class.java))
            }
            R.id.settings->
                "Setting".showToast(AllApplication.context)
            android.R.id.home->
                binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}