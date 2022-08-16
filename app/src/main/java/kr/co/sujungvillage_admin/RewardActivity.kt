package kr.co.sujungvillage_admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityRewardBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardActivity : AppCompatActivity() {

    val binding by lazy { ActivityRewardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //  토큰 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        //상벌점 작성 버튼 연결
        binding.btnManage.setOnClickListener {

        }

        binding.swipe.setOnRefreshListener {
            loadStudentData(token, binding.spinnerDormitory.selectedItem.toString())
            binding.swipe.isRefreshing = false
        }

        binding.spinnerDormitory.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.dormitory,
            R.layout.spinner_notice_dormitory
        )

        binding.spinnerDormitory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    loadStudentData(token, binding.spinnerDormitory.selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        loadStudentData(token, "전체")

    }


    fun loadStudentData(token: String, dormitory: String) {

        //학생 리스트 조회 API 연결
        RetrofitBuilder.rewardApi.studentRequest(token, dormitory)
            .enqueue(object : Callback<List<ResidentRequestResultDTO>> {
                override fun onResponse(
                    call: Call<List<ResidentRequestResultDTO>>,
                    response: Response<List<ResidentRequestResultDTO>>
                ) {
                    Log.d("REWARD_REQUEST", "학생 리스트 조회 성공")

                    //학생이 존재하지 않는 경우
                    if (response.body()?.size == 0) {
                        Log.d("REWARD_REQUEST", "학생 데이터 없음")

                    }

                    //리사이클러뷰 어댑터 연결
                    val studentList: MutableList<ResidentRequestResultDTO> = mutableListOf()

                    for (info in response.body()!!) {
                        // ★★★ 기숙사 추가
                        var student = ResidentRequestResultDTO(
                            info.userId,
                            info.name,
                            info.dormitoryName,
                            info.detailedAddress
                        )
                        studentList.add(student)
                    }
                    var adapter = RewardAdapter()
                    adapter.studentList = studentList
                    binding.recyclerReward.adapter = adapter
                    binding.recyclerReward.layoutManager =
                        LinearLayoutManager(this@RewardActivity)

                }

                override fun onFailure(
                    call: Call<List<ResidentRequestResultDTO>>,
                    t: Throwable
                ) {
                    Log.d("REWARD_REQUEST", "학생 리스트 조회 실패")
                    Log.d("REWARD_REQUEST", t.message.toString())
                    Toast.makeText(
                        this@RewardActivity,
                        "학생 리스트 조회 오류가 발생하였습니다.",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            })
    }
}

