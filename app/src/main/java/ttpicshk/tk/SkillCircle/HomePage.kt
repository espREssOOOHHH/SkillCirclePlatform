package ttpicshk.tk.SkillCircle

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ttpicshk.tk.SkillCircle.Frags.Frags_1_homePage
import ttpicshk.tk.SkillCircle.Frags.Frags_2_homePage
import ttpicshk.tk.SkillCircle.databinding.HomePageBinding
import kotlin.system.exitProcess

class HomePage:AppCompatActivity() {

    lateinit var binding:HomePageBinding
    lateinit var viewModel: MainViewModel
    private var firstQuitTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= HomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberOfFrags1=2
        val numberOfFrags2=0

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

            }
            binding.drawerLayout.closeDrawers()
            true
        }


        //fragmentManager and TabLayout
        val adapterFrag=Adapter_frags_homePage(supportFragmentManager,lifecycle)
        repeat(numberOfFrags1) {
            adapterFrag.addFrag(Frags_1_homePage("$it".toInt()))
        }
        repeat(numberOfFrags2){
            adapterFrag.addFrag(Frags_2_homePage())
        }

        binding.pagerLayout.adapter=adapterFrag
        TabLayoutMediator(binding.tabLayout,binding.pagerLayout){tab,position->
            tab.text="OBJECT ${(position+1)}"
            tab.setIcon(R.drawable.icon_model)
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
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

    private fun chooseTab(tab: TabLayout.Tab?) {
    }

    private fun recoverItemTab() {
    }

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