package kr.co.sujungvillage_admin.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kr.co.sujungvillage_admin.AlarmActivity
import kr.co.sujungvillage_admin.NoticeActivity
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.RollCallActivity
import kr.co.sujungvillage_admin.databinding.FragmentHomeBinding
import kr.co.sujungvillage_admin.retrofit.HomeInfoResultDTO
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // ★★★ 재사생 학번 불러오기
        val userNum = "99990001"

        // lottie 이미지 회전
        binding.imgWave.rotationX = 180f

        // 알림 버튼 연결
        binding.btnAlarm.setOnClickListener {
            var intent = Intent(this.activity, AlarmActivity::class.java)
            startActivity(intent)
        }

        // 홈화면 주요 기능 버튼 연결
        // 1. 공지사항 버튼 연결
        binding.btnNotice.setOnClickListener {
            var intent = Intent(this.activity, NoticeActivity::class.java)
            startActivity(intent)
        }
        // 2. 상벌점 관리 버튼 연결
        // 3. 점호 확인 버튼 연결
        binding.btnRollcall.setOnClickListener {
            var intent = Intent(this.activity, RollCallActivity::class.java)
            startActivity(intent)
        }
        // 4. 재사생 관리 버튼 연결

        // 관리자 홈 화면 정보 조회 API 연결
        RetrofitBuilder.homeApi.homeInfo(userNum, binding.calendar.currentDate.year.toString(), binding.calendar.currentDate.month.toString()).enqueue(object : Callback<HomeInfoResultDTO> {
            override fun onResponse(call: Call<HomeInfoResultDTO>, response: Response<HomeInfoResultDTO>) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 성공")
                Log.d("HOME_INFO", "user : " + response.body()?.adminInfo.toString())
                Log.d("HOME_INFO", "roll-call days : " + response.body()?.rollcallDays.toString())

                // 유저 정보 반영
                binding.textName.text = response.body()?.adminInfo?.name
                binding.textDormitory.text =
                    response.body()?.adminInfo?.dormitory + " 기숙사 " + response.body()?.adminInfo?.description + " 담당"

                // 캘린더 정보 반영
                val rollcallDecorator = RollcallDecorator(this@HomeFragment, response.body()!!.rollcallDays)
                val todayDecorator = TodayDecorator(this@HomeFragment)
                binding.calendar.addDecorators(rollcallDecorator, todayDecorator)
            }

            override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 실패")
                Log.d("HOME_INFO", t.message.toString())
            }
        })

        // 캘린더 좌우 버튼 연결
        binding.calendar.setOnMonthChangedListener { widget, date ->
            RetrofitBuilder.homeApi.homeInfo(userNum, date.year.toString(), date.month.toString()).enqueue(object : Callback<HomeInfoResultDTO> {
                override fun onResponse(call: Call<HomeInfoResultDTO>, response: Response<HomeInfoResultDTO>) {
                    Log.d("HOME_INFO", "캘린더 정보 조회 성공")
                    Log.d("HOME_INFO", "roll-call days : " + response.body()?.rollcallDays.toString())

                    // 캘린더 정보 반영
                    val rollcallDecorator = RollcallDecorator(this@HomeFragment, response.body()!!.rollcallDays)
                    val todayDecorator = TodayDecorator(this@HomeFragment)
                    binding.calendar.addDecorators(rollcallDecorator, todayDecorator)
                }

                override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                    Log.d("HOME_INFO", "캘린더 정보 조회 실패")
                    Log.d("HOME_INFO", t.message.toString())
                }
            })
        }

        return binding.root
    }
}

// 점호일 커스텀 함수
class RollcallDecorator(context: HomeFragment, days: List<Int>) : DayViewDecorator {
    val rollcallDrawable = context.resources.getDrawable(R.drawable.style_home_cal_rollcall)
    val days = days

    override fun shouldDecorate(day: CalendarDay?): Boolean { // 커스텀 여부 반환
        return days.contains(day?.day)
    }

    override fun decorate(view: DayViewFacade?) { // 커스텀 설정
        view?.setBackgroundDrawable(rollcallDrawable)
    }
}

// 오늘 커스텀 함수
class TodayDecorator(context: HomeFragment) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean { // 커스텀 여부 반환
        return return day?.equals(CalendarDay.today())!!
    }

    override fun decorate(view: DayViewFacade?) { // 커스텀 설정
        view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#FF9AE000")) {})
    }
}