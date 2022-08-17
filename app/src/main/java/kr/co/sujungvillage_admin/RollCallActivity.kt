package kr.co.sujungvillage_admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

        // Swipe Refresh 버튼 연결
        binding.swipe.setOnRefreshListener {
            loadRollcallData(token)
            binding.swipe.isRefreshing = false
        }

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener{ finish() }

        // 승인 버튼 연결
        binding.btnApprove.setOnClickListener {
            for (rollcall in selectedRollcall) {
                RetrofitBuilder.rollcallApi.rollcallChange(token, selectedRollcall,"승인").enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("ROLLCALL_APPROVE", "점호 승인 성공")
                        Log.d("ROLLCALL_APPROVE", response.body().toString())
                        Log.d("ROLLCALL_APPROVE", "code : " + response.code())
                        Log.d("ROLLCALL_APPROVE", "message : " + response.message())

                        Toast.makeText(this@RollCallActivity, "승인되었습니다.", Toast.LENGTH_SHORT).show()
                        loadRollcallData(token)
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("ROLLCALL_APPROVE", "점호 승인 실패")
                        Log.e("ROLLCALL_APPROVE", t.message.toString())
                    }
                })
            }
        }

        // 반려 버튼 연결
        binding.btnReject.setOnClickListener {
            for (rollcall in selectedRollcall) {
                RetrofitBuilder.rollcallApi.rollcallChange(token, selectedRollcall,"반려").enqueue(object: Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d("ROLLCALL_REJECT", "점호 반려 성공")
                        Log.d("ROLLCALL_REJECT", response.body().toString())

                        Toast.makeText(this@RollCallActivity, "반려되었습니다.", Toast.LENGTH_SHORT).show()
                        loadRollcallData(token)
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("ROLLCALL_REJECT", "점호 반려 실패")
                        Log.e("ROLLCALL_REJECT", t.message.toString())
                    }
                })
            }
        }
    }

    // 점호 신청 리스트 불러오기 함수
    fun loadRollcallData(token: String) {
        // 대기 중인 점호 리스트 조회 API 연결
        Log.d("DATE_TEST", CalendarDay.today().date.plusDays(-1).toString())
        RetrofitBuilder.rollcallApi.rollcallGetWaitingList(token).enqueue(object: Callback<List<RollcallGetListResultDTO>> {
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