package ttpicshk.tk.SkillCirlce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import ttpicshk.tk.SkillCirlce.databinding.AboutMeLayoutBinding

class AboutMeActivity : AppCompatActivity() {
    lateinit var binding:AboutMeLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= AboutMeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //codes about menu
        binding.navView.setCheckedItem(R.id.nav_me)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_homePage->startActivity(Intent(this,HomePage::class.java))
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        //codes about action Bar
        setSupportActionBar(binding.toolBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

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