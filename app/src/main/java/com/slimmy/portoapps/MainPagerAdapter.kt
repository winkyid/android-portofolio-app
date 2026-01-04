package com.slimmy.portoapps

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.slimmy.portoapps.ui.kontak.KontakFragment
import com.slimmy.portoapps.ui.pendidikan.PendidikanFragment
import com.slimmy.portoapps.ui.pengalaman.PengalamanFragment
import com.slimmy.portoapps.ui.portofolio.PortofolioFragment
import com.slimmy.portoapps.ui.skill.SkillFragment
import com.slimmy.portoapps.ui.tentang.TentangFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TentangFragment()
            1 -> PendidikanFragment()
            2 -> PengalamanFragment()
            3 -> SkillFragment()
            4 -> PortofolioFragment()
            5 -> KontakFragment()
            else -> TentangFragment()
        }
    }
}