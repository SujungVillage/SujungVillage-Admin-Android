package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.sujungvillage_admin.databinding.ActivityCommWriteBinding

class CommWriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityCommWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 연결
        binding.button2.setOnClickListener{ finish() }
    }
}