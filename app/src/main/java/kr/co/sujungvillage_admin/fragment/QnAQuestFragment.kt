package kr.co.sujungvillage_admin.fragment // ktlint-disable package-name

import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.adapter.QnAQuestionAdapter
import kr.co.sujungvillage_admin.data.QuestionGetResultDTO
import kr.co.sujungvillage_admin.databinding.FragmentQnAQuestBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QnAQuestFragment : Fragment() {
    var token = ""
    var questionList: MutableList<QuestionGetResultDTO> = mutableListOf()

    var _binding: FragmentQnAQuestBinding? = null
    val binding get() = _binding!!

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                loadQuestionData(token, binding.recycleQuestion)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQnAQuestBinding.inflate(inflater, container, false)

        // 로컬 변수 불러오기
        val shared =
            this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        token = shared?.getString("token", "error").toString()

        // 내 질문 리스트 불러오기
        loadQuestionData(token, binding.recycleQuestion)

        // Swipe Refresh 버튼 연결
        binding.swipe.setOnRefreshListener {
            loadQuestionData(token, binding.recycleQuestion)
            binding.swipe.isRefreshing = false
        }
        // 스크롤 업 시 리프레시 방지
        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            binding.swipe.isEnabled = binding.scroll.scrollY == 0
        }

        return binding.root
    }

    // 내 질문 리스트 조회 함수
    fun loadQuestionData(token: String, recycleQuestion: RecyclerView) {
        // 내 질문 리스트 조회 API 연결
        RetrofitBuilder.qnaApi.questionGet(token)
            .enqueue(object : Callback<List<QuestionGetResultDTO>> {
                override fun onResponse(
                    call: Call<List<QuestionGetResultDTO>>,
                    response: Response<List<QuestionGetResultDTO>>
                ) {
                    Log.d("QUESTION_GET", "질문 리스트 조회 성공")
                    Log.d("QUESTION_GET", "response : " + response.body().toString())

                    // 작성된 질문이 없는 경우
                    if (response.body()?.size == 0) {
                        binding.textExist.visibility = View.VISIBLE
                    } else {
                        binding.textExist.visibility = View.GONE
                    }

                    // 어댑터 연결
                    questionList = mutableListOf()
                    for (info in response.body()!!) {
                        questionList.add(
                            QuestionGetResultDTO(
                                info.id,
                                info.title,
                                info.date,
                                info.isAnswered
                            )
                        )
                    }
                    var adapter = QnAQuestionAdapter()
                    adapter.questionList = questionList
                    recycleQuestion.adapter = adapter
                    recycleQuestion.layoutManager = LinearLayoutManager(activity)
                }

                override fun onFailure(call: Call<List<QuestionGetResultDTO>>, t: Throwable) {
                    Log.e("QUESTION_GET", "질문 리스트 조회 실패")
                    Log.e("QUESTION_GET", t.message.toString())
                }
            })
    }
}
