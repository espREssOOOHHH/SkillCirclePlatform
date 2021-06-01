package ttpicshk.tk.SkillCircle

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class Adapter_frags_homePage(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle) {

    private val mFragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int =mFragments.size

    override fun createFragment(position: Int): Fragment =mFragments[position]

    fun addFrag(fragment: Fragment){
        mFragments.add(fragment)
    }
}