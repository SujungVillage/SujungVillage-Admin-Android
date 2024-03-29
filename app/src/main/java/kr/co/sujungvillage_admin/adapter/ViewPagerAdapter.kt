package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.sujungvillage_admin.fragment.AlarmAppFragment
import kr.co.sujungvillage_admin.fragment.AlarmCommFragment

private const val NUM_TABS = 2

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return AlarmAppFragment()
            1 -> return AlarmCommFragment()
        }
        return AlarmAppFragment()
    }
}
