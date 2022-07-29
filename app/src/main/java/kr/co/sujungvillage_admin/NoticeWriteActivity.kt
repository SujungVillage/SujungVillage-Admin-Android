package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.databinding.ActivityNoticeWriteBinding

class NoticeWriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 키보드 내리기
        binding.layout.setOnClickListener { this.hideKeyboard() }
        binding.linear.setOnClickListener { this.hideKeyboard() }

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        // 기숙사 스피너 연결 및 커스텀
        binding.spinnerDormitory.adapter = ArrayAdapter.createFromResource(this, R.array.dormitory, R.layout.spinner_notice_write_dormitory)
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