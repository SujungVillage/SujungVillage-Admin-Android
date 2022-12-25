package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.sujungvillage_admin.adapter.ViewPagerAdapter
import kr.co.sujungvillage_admin.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private val tabTitleArray = arrayOf(
        "앱 알림",
        "커뮤니티 알림"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        tabLayout.setSelectedTabIndicatorColor(Color.rgb(154, 224, 0))

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }
    }
}
