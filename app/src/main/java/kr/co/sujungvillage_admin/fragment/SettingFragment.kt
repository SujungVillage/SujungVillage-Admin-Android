package kr.co.sujungvillage_admin.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.AlarmActivity
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.databinding.FragmentSettingBinding
import kr.co.sujungvillage_admin.retrofit.HomeInfoResultDTO
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingBinding.inflate(inflater, container, false)

        //  토큰 불러오기
        val shared = this.activity?.getSharedPreferences("SujungVillage_admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 알림 버튼 연결
        binding.btnAlarm.setOnClickListener {
            var intent = Intent(this.activity, AlarmActivity::class.java)
            startActivity(intent)
        }

        // 설정 화면 정보 조회 API 연결 (홈 화면 정보 조회 API 활용)
        RetrofitBuilder.homeApi.homeInfo(token, CalendarDay.today().year.toString(), CalendarDay.today().month.toString()).enqueue(object : Callback<HomeInfoResultDTO> {
            override fun onResponse(call: Call<HomeInfoResultDTO>, response: Response<HomeInfoResultDTO>) {
                Log.d("HOME_INFO", "설정 화면 정보 조회 성공")
                Log.d("HOME_INFO", "user : " + response.body()?.adminInfo.toString())

                // 유저 정보 반영
                binding.textName.text = response.body()?.adminInfo?.name
                binding.textCharge.text = "담당 : " + response.body()?.adminInfo?.dormitory + " " + response.body()?.adminInfo?.description
            }

            override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 실패")
                Log.d("HOME_INFO", t.message.toString())
            }
        })

        return binding.root
    }
}