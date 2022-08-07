package com.example.greapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var fragments = mutableListOf(HomeFragment(), ActivityListFragment())

    fun removeAllAndAdd(fragment: Fragment) {
        fragments.clear()
        fragments.add(0,fragment)
        notifyDataSetChanged()
    }

    fun addMultiple (frag : List<Fragment>){
        fragments.clear()
        fragments.addAll(frag)
        notifyDataSetChanged()
    }

    fun add(index: Int, fragment: Fragment) {
        fragments.add(index, fragment)
        notifyItemChanged(index)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return fragments.find { it.hashCode().toLong() == itemId } != null
    }

}