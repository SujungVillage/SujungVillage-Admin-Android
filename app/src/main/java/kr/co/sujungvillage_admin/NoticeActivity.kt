package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.sujungvillage_admin.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }
    }
}