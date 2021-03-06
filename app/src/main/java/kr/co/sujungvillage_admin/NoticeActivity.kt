package kr.co.sujungvillage_admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kr.co.sujungvillage_admin.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        // 글 작성 버튼 연결
        binding.btnWrite.setOnClickListener {
            var intent = Intent(this, NoticeWriteActivity::class.java)
            startActivity(intent)
        }

        // 기숙사 스피너 연결 및 커스텀
        binding.spinnerDormitory.adapter = ArrayAdapter.createFromResource(this, R.array.dormitory, R.layout.spinner_notice_dormitory)
        binding.spinnerDormitory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    // position - 0 : 전체, 1 : 성미료, 2 : 성미관, 3 : 풍림, 4 : 엠시티, 5 : 그레이스, 6 : 이율, 7 : 장수
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }
}