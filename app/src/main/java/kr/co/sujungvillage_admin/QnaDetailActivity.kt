package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.MyqDetailGetResultDTO
import kr.co.sujungvillage_admin.data.QnaAnswerDTO
import kr.co.sujungvillage_admin.data.QnaAnswerResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityQnAdetailBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnaDetailActivity : AppCompatActivity() {
    val binding by lazy { ActivityQnAdetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 로컬 변수 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()
        var content = ""

        // 키보드 내리기
        binding.layoutQuestion.setOnClickListener { this.hideKeyboard() }
        binding.layoutAnswer.setOnClickListener { this.hideKeyboard() }
        binding.linearComment.setOnClickListener { this.hideKeyboard() }
        binding.layoutEdit.setOnClickListener { this.hideKeyboard() }
        binding.layout.setOnClickListener { this.hideKeyboard() }

        // 이전 페이지(QnAQuestFragment)에서 questionId 전달 받기
        val questionId = intent.getLongExtra("questionId", -1)
        refresh(token, questionId)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        // 등록 버튼 연결
        binding.btnRegister.setOnClickListener {
            content = binding.editAnswer.text.toString().trim()

            // 답변 내용을 작성하지 않은 경우
            if (content.isEmpty()) {
                Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // QnA 답변 작성 API 연결
            Log.d("QNA_WRITE", content)
            val qnaAnswerInfo = QnaAnswerDTO(questionId, content)
            RetrofitBuilder.qnaApi.qnaAnswer(token, qnaAnswerInfo)
                .enqueue(object : Callback<QnaAnswerResultDTO> {
                    override fun onResponse(
                        call: Call<QnaAnswerResultDTO>,
                        response: Response<QnaAnswerResultDTO>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("QNA_ANSWER", response.message().toString())
                            this@QnaDetailActivity.hideKeyboard()
                            refresh(token, questionId)
                        }
                    }

                    override fun onFailure(call: Call<QnaAnswerResultDTO>, t: Throwable) {
                        Log.d("QNA_ANSWER", t.message.toString())
                    }
                })
        }
    }

    private fun refresh(token: String, questionId: Long) {
        // 내 질문 상세 조회 API 연결
        RetrofitBuilder.qnaApi.questionDetailGet(token, questionId)
            .enqueue(object : Callback<MyqDetailGetResultDTO> {
                override fun onResponse(
                    call: Call<MyqDetailGetResultDTO>,
                    response: Response<MyqDetailGetResultDTO>
                ) {
                    Log.d("QUESTION_DETAIL", response.message())
                    Log.d("QUESTION_DETAIL", response.body().toString())
                    Log.d("QUESTION_DETAIL", response.code().toString())

                    if (response.isSuccessful) {
                        Log.d("QUESTION_DETAIL", "질문 상세 조회 성공")
                        Log.d("QUESTION_DETAIL", response.body().toString())

                        binding.textTitle.text = response.body()?.question?.title
                        binding.textDate.text = "${
                        response.body()?.question?.reqDate?.subSequence(
                            0,
                            10
                        )
                        } ${response.body()?.question?.reqDate?.subSequence(11, 16)}"
                        binding.textWriter.text =
                            "작성자 : ${if (response.body()?.question?.isAnonymous == true) "익명" else response.body()?.question?.name + "(" + response.body()?.question?.userId + ")"}"
                        binding.textContent.text = response.body()?.question?.content

                        if (response.body()?.answer == null) {
                            binding.layoutAnswer.visibility = View.GONE
                            binding.layoutEdit.visibility = View.VISIBLE
                            return
                        } else {
                            binding.layoutAnswer.visibility = View.VISIBLE
                            binding.layoutEdit.visibility = View.GONE
                        }

                        binding.textAnswerDate.text = "${
                        response.body()?.answer?.regDate?.subSequence(
                            0,
                            10
                        )
                        } ${response.body()?.answer?.regDate?.subSequence(11, 16)}"
                        binding.textAnswer.text = response.body()?.answer?.content
                    } else {
                        val builder =
                            androidx.appcompat.app.AlertDialog.Builder(this@QnaDetailActivity)
                        builder.setTitle("글이 존재하지 않습니다.")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    Log.d("COMM_DETAIL", "글이 존재하지 않음")
                                    finish()
                                }
                            )
                        builder.show()
                    }
                }

                override fun onFailure(call: Call<MyqDetailGetResultDTO>, t: Throwable) {
                    Log.e("QUESTION_DETAIL", "질문 상세 조회 실패")
                    Log.e("QUESTION_DETAIL", t.message.toString())

                    Toast.makeText(this@QnaDetailActivity, "불러올 수 없는 질문입니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            })
    }
}
