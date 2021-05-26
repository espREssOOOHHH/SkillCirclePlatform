package ttpicshk.tk.SkillCirlce

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ttpicshk.tk.SkillCirlce.Frags.Frags_1_homePage
import ttpicshk.tk.SkillCirlce.Frags.Frags_2_homePage
import ttpicshk.tk.SkillCirlce.databinding.HomePageBinding
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
                R.id.nav_comment->startActivity(Intent(this,LogInActivity::class.java))
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
        binding.navView.setCheckedItem(R.id.nav_homePage)
        super.onResume()
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
        val iconDrawer=findViewById<View>(R.id.menu_icon)
        iconDrawer.setOnClickListener {
            startActivity(Intent(this,AboutMeActivity::class.java))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.backup-> {
                Toast.makeText(AllApplication.context,"💽", Toast.LENGTH_SHORT).show()
            }
            R.id.settings->
                "Setting".showToast(AllApplication.context)
            android.R.id.home->
                binding.drawerLayout.openDrawer(GravityCompat.START)
                   }
        return true
    }
}