package kr.co.sujungvillage_admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.adapter.RollcallAdapter
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityCommWriteBinding
import kr.co.sujungvillage_admin.databinding.ActivityRollCallBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RollCallActivity : AppCompatActivity() {
    val binding by lazy { ActivityRollCallBinding.inflate(layoutInflater) }

    companion object {
        var selectedRollcall: MutableList<Long> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 로컬에서 토큰 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 대기 중인 점호 리스트 불러오기
        loadRollcallData(token)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener{ finish() }
    }

    // 점호 신청 리스트 불러오기 함수
    fun loadRollcallData(token: String) {
        // 점호 신청 리스트 조회 API 연결 (★★★ 대기 중인 점호 리스트로 수정하기)
        RetrofitBuilder.rollcallApi.rollcallGetList(token, CalendarDay.today().date.plusDays(-1).toString(), "대기").enqueue(object: Callback<List<RollcallGetListResultDTO>> {
            override fun onResponse(call: Call<List<RollcallGetListResultDTO>>, response: Response<List<RollcallGetListResultDTO>>) {
                Log.d("ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 성공")
                Log.d("ROLLCALL_LIST_REQUEST", response.body().toString())

                if (response.body()?.size == 0) {
                    binding.textExist.visibility = View.VISIBLE
                } else {
                    binding.textExist.visibility = View.GONE
                }

                var rollcallList: MutableList<RollcallGetListResultDTO> = mutableListOf()
                for (info in response.body()!!) {
                    rollcallList.add(RollcallGetListResultDTO(info.id, info.userId, info.userName, info.dormitory, info.address, info.image, info.location, info.date, info.state))
                }
                val adapter = RollcallAdapter()
                adapter.rollcallList = rollcallList
                binding.recycleRollcall.adapter = adapter
                binding.recycleRollcall.layoutManager = LinearLayoutManager(this@RollCallActivity)
            }

            override fun onFailure(call: Call<List<RollcallGetListResultDTO>>, t: Throwable) {
                Log.e("ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 실패")
                Log.e("ROLLCALL_LIST_REQUEST", t.message.toString())
            }
        })
    }
}