package kr.co.sujungvillage_admin.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.AlarmActivity
import kr.co.sujungvillage_admin.LoginActivity
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
        val shared = this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()
        val read = shared?.getBoolean("alarmRead", true)

        // 알림 버튼 연결
        binding.btnAlarm.setOnClickListener {
            var intent = Intent(this.activity, AlarmActivity::class.java)
            startActivity(intent)
        }

        // 읽음/안 읽음 처리
        if (!read!!) {
            binding.imgUnread.visibility = View.VISIBLE
        } else {
            binding.imgUnread.visibility = View.INVISIBLE
        }

        // 알람 버튼 초기화
        if(shared!!.getBoolean("alarm", true)) {
            binding.switchAlarm.isChecked = true
        }

        // 알람 설정 버튼 연결
        binding.switchAlarm.setOnCheckedChangeListener { button, check ->
            val editor = shared?.edit()
            if (check) editor?.putBoolean("alarm", true)
            else editor?.putBoolean("alarm", false)
            editor?.apply()
        }

        // 성신 포탈 버튼 연결
        binding.layoutPortal.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.sungshin.ac.kr/sso/login.jsp"))
            startActivity(intent)
        }
        // 앱 사용법 버튼 연결
        binding.layoutManual.setOnClickListener {
            Toast.makeText(this.activity, "준비 중인 기능입니다!", Toast.LENGTH_SHORT).show()
        }
        // 앱 문의하기 버튼 연결
        binding.layoutInquire.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("sujungvillage@gmail.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            startActivity(email)
        }
        // 로그아웃 버튼 연결
        binding.layoutLeave.setOnClickListener {
            Toast.makeText(activity, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
            val editor = shared.edit()
            editor.remove("token")
            editor.apply()
            var intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        // 설정 화면 정보 조회 API 연결 (홈 화면 정보 조회 API 활용)
        RetrofitBuilder.homeApi.homeInfo(token, CalendarDay.today().year.toString(), CalendarDay.today().month.toString()).enqueue(object : Callback<HomeInfoResultDTO> {
            override fun onResponse(call: Call<HomeInfoResultDTO>, response: Response<HomeInfoResultDTO>) {
                Log.d("HOME_INFO", "설정 화면 정보 조회 성공")
                Log.d("HOME_INFO", "user : " + response.body()?.adminInfo.toString())

                // 유저 정보 반영
                binding.textName.text = response.body()?.adminInfo?.name
                if (response.body()?.adminInfo?.dormitory == "전체" && response.body()?.adminInfo?.description == "전체")
                    binding.textCharge.text = "담당 : " + response.body()?.adminInfo?.dormitory
                else binding.textCharge.text = "담당 : " + response.body()?.adminInfo?.dormitory + " " + response.body()?.adminInfo?.description
            }

            override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 실패")
                Log.d("HOME_INFO", t.message.toString())
            }
        })

        return binding.root
    }
}