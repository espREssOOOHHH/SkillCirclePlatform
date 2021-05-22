package ttpicshk.tk.SkillCirlce

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

class HomePage:AppCompatActivity() {

    lateinit var binding:HomePageBinding
    lateinit var viewModel: MainViewModel

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
                R.id.nav_me->startActivity(Intent(this,AboutMeActivity::class.java))
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

    //codes about menu button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
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
            R.id.menu_icon->
                startActivity(Intent(this,AboutMeActivity::class.java))
        }
        return true
    }
}