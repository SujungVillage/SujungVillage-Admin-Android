package kr.co.sujungvillage_admin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.adapter.RollcallAdapter
import kr.co.sujungvillage_admin.data.RollcallChangeDTO
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.FragmentCurrentRollCallBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentRollCall.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentRollCall : Fragment() {
    companion object {
        var selectedRollcall: MutableList<Long> = mutableListOf()
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentRollCall.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CurrentRollCall().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    val binding by lazy { FragmentCurrentRollCallBinding.inflate(layoutInflater) }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val binding = FragmentCurrentRollCallBinding.inflate(inflater, container, false)
        // 로컬에서 토큰 불러오기
        val shared = this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
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
            RetrofitBuilder.rollcallApi.rollcallChange(token, RollcallChangeDTO(selectedRollcall, "승인")).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("ROLLCALL_APPROVE", "점호 승인 성공")
                    Log.d("ROLLCALL_APPROVE", response.body().toString())
                    Log.d("ROLLCALL_APPROVE", "code : " + response.code())
                    Log.d("ROLLCALL_APPROVE", "message : " + response.message())

                    Toast.makeText(activity, "승인되었습니다.", Toast.LENGTH_SHORT).show()
                    loadRollcallData(token)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ROLLCALL_APPROVE", "점호 승인 실패")
                    Log.e("ROLLCALL_APPROVE", t.message.toString())
                }
            })
        }

        // 반려 버튼 연결
        binding.btnReject.setOnClickListener {
            RetrofitBuilder.rollcallApi.rollcallChange(token, RollcallChangeDTO(selectedRollcall, "반려")).enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("ROLLCALL_REJECT", "점호 반려 성공")
                    Log.d("ROLLCALL_REJECT", response.body().toString())

                    Toast.makeText(activity, "반려되었습니다.", Toast.LENGTH_SHORT).show()
                    loadRollcallData(token)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ROLLCALL_REJECT", "점호 반려 실패")
                    Log.e("ROLLCALL_REJECT", t.message.toString())
                }
            })
        }
        return binding.root
    }

    // 점호 신청 리스트 불러오기 함수
    fun loadRollcallData(token: String) {
        // 대기 중인 점호 리스트 조회 API 연결
        Log.d("DATE_TEST", CalendarDay.today().date.plusDays(-1).toString())
        RetrofitBuilder.rollcallApi.rollcallGetWaitingList(token).enqueue(object: Callback<List<RollcallGetListResultDTO>> {
            override fun onResponse(call: Call<List<RollcallGetListResultDTO>>, response: Response<List<RollcallGetListResultDTO>>) {
                Log.d("ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 성공")
                //Log.d("ROLLCALL_LIST_REQUEST", response.body().toString())

                // 선택 점호 초기화
                selectedRollcall.clear()

                if (response.body()?.size == 0) {
                    binding.textExist.visibility = View.VISIBLE
                } else {
                    binding.textExist.visibility = View.GONE
                }

                var rollcallList: MutableList<RollcallGetListResultDTO> = mutableListOf()
                for (info in response.body()!!) {
                    Log.d("ROLLCALL_LIST_REQUEST", info.toString())
                    rollcallList.add(RollcallGetListResultDTO(info.id, info.userId, info.userName, info.dormitory, info.address, info.image, info.location, info.date, info.state))
                }
                val adapter = RollcallAdapter()
                adapter.rollcallList = rollcallList
                binding.recycleRollcall.adapter = adapter
                binding.recycleRollcall.layoutManager = LinearLayoutManager(activity)
            }

            override fun onFailure(call: Call<List<RollcallGetListResultDTO>>, t: Throwable) {
                Log.e("ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 실패")
                Log.e("ROLLCALL_LIST_REQUEST", t.message.toString())
            }
        })
    }

}

