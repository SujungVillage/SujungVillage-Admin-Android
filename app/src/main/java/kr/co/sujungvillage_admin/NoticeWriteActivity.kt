package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.NoticeCreateDTO
import kr.co.sujungvillage_admin.data.NoticeCreateResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityNoticeWriteBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeWriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityNoticeWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //  토큰 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 키보드 내리기
        binding.layout.setOnClickListener { this.hideKeyboard() }
        binding.linear.setOnClickListener { this.hideKeyboard() }

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        // 기숙사 스피너 연결 및 커스텀
        binding.spinnerDormitory.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.dormitory,
            R.layout.spinner_notice_write_dormitory
        )

        // 등록 버튼 연결 : 공지사항 API 연결 및 액티비티 종료
        binding.btnUpload.setOnClickListener {
            // 비어있는 항목이 있는 경우
            if (binding.editTitle.text.isEmpty()) {
                Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.editContent.text.isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()
            var dormitory = binding.spinnerDormitory.selectedItem.toString()
            var noticeInfo = NoticeCreateDTO(title, content, dormitory)
            Log.d("NOTICE_WRITE_REQUEST", noticeInfo.toString())

            RetrofitBuilder.noticeApi.noticeCreate(token, noticeInfo)
                .enqueue(object : Callback<NoticeCreateResultDTO> {
                    override fun onResponse(
                        call: Call<NoticeCreateResultDTO>,
                        response: Response<NoticeCreateResultDTO>
                    ) {
                        Log.d("NOTICE_CREATE", "공지사항 작성 요청 성공")
                        Log.d("NOTICE_CREATE", "id : " + response.body()?.id)
                        Log.d("NOTICE_CREATE", "writer : " + response.body()?.writer)
                        Log.d("NOTICE_CREATE", "title : " + response.body()?.title)
                        Log.d("NOTICE_CREATE", "content : " + response.body()?.content)
                        Log.d("NOTICE_CREATE", "register date : " + response.body()?.regDate)
                        Log.d("NOTICE_CREATE", "modify date : " + response.body()?.modDate)
                        Log.d("NOTICE_CREATE", "dormitory : " + response.body()?.dormitory)

                        Toast.makeText(
                            this@NoticeWriteActivity,
                            "공지사항이 작성되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    override fun onFailure(call: Call<NoticeCreateResultDTO>, t: Throwable) {
                        Log.d("NOTICE_CREATE", "공지사항 작성 요청 실패")
                        Log.d("NOTICE_CREATE", t.message.toString())
                    }
                })
        }
    }
}
