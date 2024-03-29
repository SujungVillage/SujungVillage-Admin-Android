package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.adapter.RollcallAdapter
import kr.co.sujungvillage_admin.data.RollcallChangeDTO
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.FragmentCurrentRollCallBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentRollCall : Fragment() {
    companion object {
        var selectedRollcall: MutableList<Long> = mutableListOf()
    }

    val binding by lazy { FragmentCurrentRollCallBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val binding = FragmentCurrentRollCallBinding.inflate(inflater, container, false)
        // 로컬에서 토큰 불러오기
        val shared =
            this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()
        // 대기 중인 점호 리스트 불러오기
        loadRollcallData(token)

        /*// Swipe Refresh 버튼 연결
        binding.swipe.setOnRefreshListener {
            loadRollcallData(token)
            binding.swipe.isRefreshing = false
        }
        // 스크롤 업 시 리프레시 방지
        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            binding.swipe.isEnabled = binding.scroll.scrollY == 0
        }*/

        // 승인 버튼 연결
        binding.btnApprove.setOnClickListener {
            RetrofitBuilder.rollcallApi.rollcallChange(
                token,
                RollcallChangeDTO(selectedRollcall, "승인")
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("C_ROLLCALL_APPROVE", "점호 승인 성공")
                    Log.d("C_ROLLCALL_APPROVE", response.body().toString())
                    Log.d("C_ROLLCALL_APPROVE", "code : " + response.code())
                    Log.d("C_ROLLCALL_APPROVE", "message : " + response.message())

                    Toast.makeText(activity, "승인되었습니다.", Toast.LENGTH_SHORT).show()
                    loadRollcallData(token)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("C_ROLLCALL_APPROVE", "점호 승인 실패")
                    Log.e("C_ROLLCALL_APPROVE", t.message.toString())
                }
            })
        }

        // 반려 버튼 연결
        binding.btnReject.setOnClickListener {
            RetrofitBuilder.rollcallApi.rollcallChange(
                token,
                RollcallChangeDTO(selectedRollcall, "반려")
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("C_ROLLCALL_REJECT", "점호 반려 성공")
                    Log.d("C_ROLLCALL_REJECT", response.body().toString())

                    Toast.makeText(activity, "반려되었습니다.", Toast.LENGTH_SHORT).show()
                    loadRollcallData(token)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("C_ROLLCALL_REJECT", "점호 반려 실패")
                    Log.e("C_ROLLCALL_REJECT", t.message.toString())
                }
            })
        }
        return binding.root
    }

    // 점호 신청 리스트 불러오기 함수
    fun loadRollcallData(token: String) {
        // 대기 중인 점호 리스트 조회 API 연결
        Log.d("DATE_TEST", CalendarDay.today().date.plusDays(-1).toString())
        RetrofitBuilder.rollcallApi.rollcallGetWaitingList(token)
            .enqueue(object : Callback<List<RollcallGetListResultDTO>> {
                override fun onResponse(
                    call: Call<List<RollcallGetListResultDTO>>,
                    response: Response<List<RollcallGetListResultDTO>>
                ) {
                    Log.d("C_ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 성공")
                    // Log.d("ROLLCALL_LIST_REQUEST", response.body().toString())

                    // 선택 점호 초기화
                    selectedRollcall.clear()

                    if (response.body()?.size == 0) {
                        binding.textExist.visibility = View.VISIBLE
                    } else {
                        binding.textExist.visibility = View.GONE
                    }

                    var rollcallList: MutableList<RollcallGetListResultDTO> = mutableListOf()
                    for (info in response.body()!!) {
                        rollcallList.add(
                            RollcallGetListResultDTO(
                                info.id,
                                info.userId,
                                info.userName,
                                info.dormitory,
                                info.address,
                                info.image,
                                info.location,
                                info.date,
                                info.state
                            )
                        )
                    }
                    val adapter = RollcallAdapter()
                    adapter.rollcallList = rollcallList
                    binding.recycleRollcall.adapter = adapter
                    binding.recycleRollcall.layoutManager = LinearLayoutManager(activity)
                }

                override fun onFailure(call: Call<List<RollcallGetListResultDTO>>, t: Throwable) {
                    Log.e("C_ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 실패")
                    Log.e("C_ROLLCALL_LIST_REQUEST", t.message.toString())
                }
            })
    }
}
