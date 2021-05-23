package ttpicshk.tk.SkillCirlce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import ttpicshk.tk.SkillCirlce.databinding.AboutMeLayoutBinding

class AboutMeActivity : AppCompatActivity() {
    lateinit var binding:AboutMeLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!Account.IsOnLine())
        {

        }
        binding= AboutMeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Account.LogIn("Jackson","20392984","12498792")

        val userName=Account.userName()
        val userPhoto=Account.userPhoto()
        setSupportActionBar(binding.toolbarAboutMe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapseToolBarAboutMe.title=userName
        Glide.with(this).load(userPhoto).into(binding.userPhotoAboutMe)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}