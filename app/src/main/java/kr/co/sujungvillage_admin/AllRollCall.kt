package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.adapter.RollcallAdapter
import kr.co.sujungvillage_admin.data.RollcallChangeDTO
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.FragmentAllRollCallBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AllRollCall : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val binding by lazy { FragmentAllRollCallBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 로컬에서 토큰 불러오기
        val shared =
            this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 날짜 받아오기
        val myDate = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        val today = Date()
        calendar.time = today
        binding.txtDate.text = "${myDate.format(calendar.time)}"

        binding.spinnerRollCall.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.rollcall_type,
            R.layout.spinner_notice_write_dormitory
        )
        var state = binding.spinnerRollCall.selectedItem.toString()
        loadAllRollCallData(token, myDate.format(calendar.time), state)

        binding.spinnerRollCall.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    state = binding.spinnerRollCall.selectedItem.toString()
                    loadAllRollCallData(token, myDate.format(calendar.time), state)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.btnYesterday.setOnClickListener {
            calendar.add(Calendar.DATE, -1) // 하루 전
            binding.txtDate.text = "${myDate.format(calendar.time)}"
            // 스피너값 초기화
            binding.spinnerRollCall.setSelection(0)
            state = binding.spinnerRollCall.selectedItem.toString()
            loadAllRollCallData(token, myDate.format(calendar.time), state)
        }
        binding.btnTomorrow.setOnClickListener {
            calendar.add(Calendar.DATE, 1) // 하루 후
            binding.txtDate.text = "${myDate.format(calendar.time)}"
            // 스피너값 초기화
            binding.spinnerRollCall.setSelection(0)
            state = binding.spinnerRollCall.selectedItem.toString()
            loadAllRollCallData(token, myDate.format(calendar.time), state)
        }
        // 승인 버튼 연결
        binding.btnApprove.setOnClickListener {
            RetrofitBuilder.rollcallApi.rollcallChange(
                token,
                RollcallChangeDTO(CurrentRollCall.selectedRollcall, "승인")
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("A_ROLLCALL_APPROVE", "점호 승인 성공")
                    Log.d("A_ROLLCALL_APPROVE", response.body().toString())
                    Log.d("A_ROLLCALL_APPROVE", "code : " + response.code())
                    Log.d("A_ROLLCALL_APPROVE", "message : " + response.message())

                    Toast.makeText(activity, "승인되었습니다.", Toast.LENGTH_SHORT).show()
                    loadAllRollCallData(token, myDate.format(calendar.time), state)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("A_ROLLCALL_APPROVE", "점호 승인 실패")
                    Log.e("A_ROLLCALL_APPROVE", t.message.toString())
                }
            })
        }
        // 반려 버튼 연결
        binding.btnReject.setOnClickListener {
            RetrofitBuilder.rollcallApi.rollcallChange(
                token,
                RollcallChangeDTO(CurrentRollCall.selectedRollcall, "반려")
            ).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d("C_ROLLCALL_REJECT", "점호 반려 성공")
                    Log.d("C_ROLLCALL_REJECT", response.body().toString())

                    Toast.makeText(activity, "반려되었습니다.", Toast.LENGTH_SHORT).show()
                    loadAllRollCallData(token, myDate.format(calendar.time), state)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("C_ROLLCALL_REJECT", "점호 반려 실패")
                    Log.e("C_ROLLCALL_REJECT", t.message.toString())
                }
            })
        }

        return binding.root
    }

    fun loadAllRollCallData(token: String, date: String, state: String) {
        Log.d("TEST", date)
        Log.d("TEST", state)
        RetrofitBuilder.rollcallApi.rollcallGetList(token, date, state)
            .enqueue(object : Callback<List<RollcallGetListResultDTO>> {
                override fun onResponse(
                    call: Call<List<RollcallGetListResultDTO>>,
                    response: Response<List<RollcallGetListResultDTO>>
                ) {
                    Log.d("A_ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 성공")
                    // 선택 점호 초기화
                    CurrentRollCall.selectedRollcall.clear()
                    if (response.body()?.size == 0) {
                        binding.textExist.visibility = View.VISIBLE
                    } else {
                        binding.textExist.visibility = View.GONE
                    }
                    if (response.isSuccessful) {
                        var rollcallList: MutableList<RollcallGetListResultDTO> = mutableListOf()
                        for (info in response.body()!!) {
                            Log.d("A_ROLLCALL_LIST_REQUEST", info.state)
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
                    } else {
                        Log.d("A_ROLLCALL_LIST_REQUEST", response.toString())
                        Log.d("A_ROLLCALL_LIST_REQUEST", response.message())
                        Log.d("A_ROLLCALL_LIST_REQUEST", response.body().toString())
                        Log.d("A_ROLLCALL_LIST_REQUEST", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<List<RollcallGetListResultDTO>>, t: Throwable) {
                    Log.e("A_ROLLCALL_LIST_REQUEST", "점호 신청 리스트 조회 실패")
                    Log.e("A_ROLLCALL_LIST_REQUEST", t.message.toString())
                }
            })
    }
}
