package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.sujungvillage_admin.data.FaqWriteRequestDTO
import kr.co.sujungvillage_admin.data.FaqWriteResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityFaqwriteBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQWriteActivity : AppCompatActivity() {
    val binding by lazy { ActivityFaqwriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()
        var dormitory = shared?.getString("dormitory", "error").toString()

        binding.btnUpload.setOnClickListener {
            // 버튼 클릭시 제목과 내용 받아오기.
            val question = binding.editTitle.text.toString().trim() // 앞뒤 공백 제거
            val answer = binding.editContent.text.toString().trim() // 앞뒤 공백 제거

            // 내용이 null인지 확인 필요
            if (question.isEmpty()) {
                if (answer.isEmpty()) {
                    Toast.makeText(
                        this,
                        "제목과 내용을 입력하세요. $question, $answer",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, "제목을 입력하세요. $question, $answer", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (answer.isEmpty()) {
                    Toast.makeText(this, "내용을 입력하세요. $question, $answer", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // 서버에 보내기
                    val faqWriteInfo = FaqWriteRequestDTO(question, answer, dormitory)
                    RetrofitBuilder.qnaApi.faqWrite(token, faqWriteInfo)
                        .enqueue(object : Callback<FaqWriteResultDTO> {
                            override fun onResponse(
                                call: Call<FaqWriteResultDTO>,
                                response: Response<FaqWriteResultDTO>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("FAQ_WRITE", response.body().toString())
                                    finish()
                                }
                            }

                            override fun onFailure(call: Call<FaqWriteResultDTO>, t: Throwable) {
                                Log.d("FAQ_WRITE", t.message.toString())
                            }
                        })
                }
            }
        }
    }
}
