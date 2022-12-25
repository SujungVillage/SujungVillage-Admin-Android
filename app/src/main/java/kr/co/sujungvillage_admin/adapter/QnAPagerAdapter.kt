package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.sujungvillage_admin.fragment.AlarmAppFragment
import kr.co.sujungvillage_admin.fragment.QnAFaqFragment
import kr.co.sujungvillage_admin.fragment.QnAQuestFragment

private const val NUM_TABS = 2

class QnAPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return QnAFaqFragment()
            1 -> return QnAQuestFragment()
        }
        return AlarmAppFragment()
    }
}
