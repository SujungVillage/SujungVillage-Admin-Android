package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.sujungvillage_admin.databinding.ActivityCommWriteBinding
import kr.co.sujungvillage_admin.databinding.ActivityRollCallBinding

class RollCallActivity : AppCompatActivity() {
    val binding by lazy { ActivityRollCallBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener{ finish() }
    }
}