package ttpicshk.tk.SkillCircle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCircle.databinding.SettingsLayoutBinding

class SettingPage : AppCompatActivity() {
    lateinit var binding:SettingsLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= SettingsLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //codes about action Bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        //codes about menu
        binding.navView.setCheckedItem(R.id.nav_settings)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_homePage->startActivity(Intent(this,HomePage::class.java))
                R.id.nav_aboutMe->startActivity(Intent(this,AboutMeActivity::class.java))
                R.id.nav_settings->startActivity(Intent(this,SettingPage::class.java))

            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onResume() {
        invalidateOptionsMenu()
        binding.navView.setCheckedItem(R.id.nav_settings)
        super.onResume()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val iconDrawer=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.menu_icon)
        val background=findViewById<ImageView>(R.id.menu_background)
        Glide.with(AllApplication.context).load(Account.userPhoto()).into(iconDrawer)
        Glide.with(AllApplication.context).load(Account.userBackGround()).into(background)
        val name=findViewById<TextView>(R.id.menu_userText)
        name.text=Account.userName()
        val signature=findViewById<TextView>(R.id.menu_signature)
        signature.text=Account.signature()
        iconDrawer.setOnClickListener {
            startActivity(Intent(this,AboutMeActivity::class.java))
        }
        return true
    }
}