package kr.co.sujungvillage_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.adapter.RewardSelectedAdapter
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.SelectedUser
import kr.co.sujungvillage_admin.databinding.ActivityRewardWriteBinding

class RewardWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityRewardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 선택한 재사생 정보 불러오기
        val studentList = intent.getSerializableExtra("rewards") as MutableList<SelectedUser>
        var adapter = RewardSelectedAdapter()
        adapter.studentList = studentList
        binding.recyclerStudents.adapter = adapter
        binding.recyclerStudents.layoutManager = LinearLayoutManager(this)

        // 키보드 내리기
        binding.layout.setOnClickListener { this.hideKeyboard() }
        binding.linearReward.setOnClickListener { this.hideKeyboard() }
        binding.layoutToggle.setOnClickListener { this.hideKeyboard() }

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener {
            finish()
            val intent = Intent(this, RewardActivity::class.java)
            startActivity(intent)
        }

        // 등록 버튼 연결
        binding.btnRegister.setOnClickListener {

        }
    }
}