package kr.co.sujungvillage_admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.adapter.NoticeAdapter
import kr.co.sujungvillage_admin.data.NoticeRequestResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityNoticeBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //  토큰 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

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
                loadNoticeData(token, binding.spinnerDormitory.selectedItem.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }

        // 공지사항 리스트 불러오기
        loadNoticeData(token, "전체")

        // Swipe Refresh 버튼 연결
        binding.swipe.setOnRefreshListener {
            loadNoticeData(token, binding.spinnerDormitory.selectedItem.toString())
            binding.swipe.isRefreshing = false
        }
    }

    // 공지사항 리스트 불러오기 함수
    fun loadNoticeData(token: String, dormitory: String) {
        // 공지사항 리스트 조회 API 연결
        RetrofitBuilder.noticeApi.noticeRequest(token, dormitory).enqueue(object: Callback<List<NoticeRequestResultDTO>> {
            override fun onResponse(call: Call<List<NoticeRequestResultDTO>>, response: Response<List<NoticeRequestResultDTO>>) {
                Log.d("NOTICE_REQUEST", "공지사항 리스트 조회 성공")
                Log.d("NOTICE_REQUEST", response.body().toString())

                // 공지사항이 존재하지 않는 경우
                if (response.body()?.size == 0) {
                    binding.textNone.visibility = View.VISIBLE
                } else {
                    binding.textNone.visibility = View.GONE
                }

                // 리사이클러뷰 어댑터 연결
                val noticeList: MutableList<NoticeRequestResultDTO> = mutableListOf()
                for (info in response.body()!!) {
                    // ★★★ 기숙사 추가
                    var notice = NoticeRequestResultDTO(info.id, info.title, info.date)
                    noticeList.add(notice)
                }
                var adapter = NoticeAdapter()
                adapter.noticeList = noticeList
                binding.recyclerNotice.adapter = adapter
                binding.recyclerNotice.layoutManager = LinearLayoutManager(this@NoticeActivity)
            }

            override fun onFailure(call: Call<List<NoticeRequestResultDTO>>, t: Throwable) {
                Log.d("NOTICE_REQUEST", "공지사항 리스트 조회 실패")
                Log.d("NOTICE_REQUEST", t.message.toString())
                Toast.makeText(this@NoticeActivity, "공지사항 조회 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}