package kr.co.sujungvillage_admin.fragment // ktlint-disable package-name

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kr.co.sujungvillage_admin.* // ktlint-disable no-wildcard-imports
import kr.co.sujungvillage_admin.data.HomeInfoResultDTO
import kr.co.sujungvillage_admin.data.RollcallCreateDTO
import kr.co.sujungvillage_admin.data.RollcallCreateResultDTO
import kr.co.sujungvillage_admin.data.RollcallGetDateResultDTO
import kr.co.sujungvillage_admin.databinding.FragmentHomeBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    var dayRollcall: MutableList<Int>? = mutableListOf()
    var idRollcall: MutableList<Long>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        //  로컬 변수 불러오기
        val shared =
            this.activity?.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()
        var dormitory = shared?.getString("dormitory", "error").toString()
        val read = shared?.getBoolean("alarmRead", true)

        // lottie 이미지 회전
        binding.imgWave.rotationX = 180f

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

        // 홈화면 주요 기능 버튼 연결
        // 1. 공지사항 버튼 연결
        binding.btnNotice.setOnClickListener {
            var intent = Intent(this.activity, NoticeActivity::class.java)
            startActivity(intent)
        }
        // 2. 상벌점 관리 버튼 연결
        binding.btnReward.setOnClickListener {
            var intent = Intent(this.activity, RewardActivity::class.java)
            startActivity(intent)
        }
        // 3. 점호 확인 버튼 연결
        binding.btnRollcall.setOnClickListener {
            var intent = Intent(this.activity, RollCallActivity::class.java)
            startActivity(intent)
        }
        // 4. 재사생 관리 버튼 연결
        binding.btnStudentManage.setOnClickListener {
            Toast.makeText(activity, "준비 중인 기능입니다!", Toast.LENGTH_SHORT).show()
        }

        // Swipe Refresh 버튼 연결
        binding.swipe.setOnRefreshListener {
            loadCalendarData(
                token,
                binding.calendar.currentDate.year.toString(),
                binding.calendar.currentDate.month.toString(),
                binding.calendar
            )
            binding.swipe.isRefreshing = false
        }
        // 스크롤 업 시 리프레시 방지
        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            binding.swipe.isEnabled = binding.scroll.scrollY == 0
        }

        // 캘린더 좌우 버튼 연결
        binding.calendar.setOnMonthChangedListener { widget, date ->
            loadCalendarData(token, date.year.toString(), date.month.toString(), binding.calendar)
        }

        // 관리자 홈 화면 정보 조회 API 연결
        RetrofitBuilder.homeApi.homeInfo(
            token,
            binding.calendar.currentDate.year.toString(),
            binding.calendar.currentDate.month.toString()
        ).enqueue(object : Callback<HomeInfoResultDTO> {
            override fun onResponse(
                call: Call<HomeInfoResultDTO>,
                response: Response<HomeInfoResultDTO>
            ) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 성공")
                Log.d("HOME_INFO", "user : " + response.body()?.adminInfo.toString())
                Log.d("HOME_INFO", "roll-call days : " + response.body()?.rollcallDays.toString())
                Log.e("HOME_INFO", "code : " + response.code().toString())
                Log.e("HOME_INFO", "message : " + response.message())
                dayRollcall?.clear()
                idRollcall?.clear()
                for (rollcallDay in response.body()?.rollcallDays!!) {
                    dayRollcall?.add(rollcallDay.day)
                    idRollcall?.add(rollcallDay.id)
                }

                // 유저 정보 반영
                binding.textName.text = response.body()?.adminInfo?.name
                if (response.body()?.adminInfo?.dormitory == "전체" && response.body()?.adminInfo?.dormitory == "전체") {
                    binding.textDormitory.text = response.body()?.adminInfo?.dormitory + " 담당"
                } else binding.textDormitory.text =
                    response.body()?.adminInfo?.dormitory + " 기숙사 " + response.body()?.adminInfo?.description + " 담당"

                // 로컬에 담당 기숙사 저장
                val editor = shared?.edit()
                editor?.putString("dormitory", response.body()?.adminInfo?.dormitory)
                editor?.apply()
                dormitory = shared?.getString("dormitory", "error").toString()

                // 캘린더 정보 반영
                val rollcallDecorator = RollcallDecorator(
                    this@HomeFragment,
                    dayRollcall!!,
                    binding.calendar.currentDate.month
                )
                val todayDecorator = TodayDecorator(this@HomeFragment)
                binding.calendar.addDecorators(rollcallDecorator, todayDecorator)
            }

            override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                Log.d("HOME_INFO", "홈 화면 정보 조회 실패")
                Log.d("HOME_INFO", t.message.toString())
            }
        })

        // 날짜 클릭 이벤트
        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            // 점호일인 경우
            if (dayRollcall?.contains(date.day) == true) {
                // 점호일 조회 API 연결
                val rollcallId = idRollcall!!.get(dayRollcall!!.indexOf(date.day))
                RetrofitBuilder.rollcallApi.rollcallGetDate(token, rollcallId)
                    .enqueue(object : Callback<RollcallGetDateResultDTO> {
                        override fun onResponse(
                            call: Call<RollcallGetDateResultDTO>,
                            response: Response<RollcallGetDateResultDTO>
                        ) {
                            Log.d("ROLLCALL_CHECK", "점호일 조회 성공")
                            Log.d("ROLLCALL_CHECK", "response : " + response.body().toString())

                            // null 값이 반환되면 무시
                            if (response.body()?.id == null) {
                                Log.d("ROLLCALL_CHECK", "점호가 없는 날짜입니다.")
                                return
                            }

                            // 점호 Alert Dialog 레이아웃 설정 및 생성
                            val dialogLayout = layoutInflater.inflate(
                                R.layout.layout_calendar_rollcall_check,
                                null
                            )
                            val builder =
                                AlertDialog.Builder(context).apply { setView(dialogLayout) }
                            val dialog = builder.create()
                            dialog.show()

                            // Alert Dialog 점호 정보 설정
                            dialogLayout.findViewById<TextView>(R.id.text_title).text =
                                "${date.date} 점호"
                            dialogLayout.findViewById<TextView>(R.id.text_dormitory).text =
                                "•  점호 대상 : ${response.body()?.dormitory} 기숙사"
                            dialogLayout.findViewById<TextView>(R.id.btn_confirm)
                                .setOnClickListener { dialog.dismiss() }
                            // 오늘 또는 오늘 이후 날짜만 외박 취소 가능
                            if (date.isAfter(CalendarDay.today()) || date.equals(CalendarDay.today())) {
                                dialogLayout.findViewById<TextView>(R.id.btn_cancel)
                                    .setOnClickListener {
                                        // 점호 삭제 API 연결
                                        RetrofitBuilder.rollcallApi.rollcallDelete(
                                            token,
                                            rollcallId
                                        ).enqueue(object : Callback<Void> {
                                            override fun onResponse(
                                                call: Call<Void>,
                                                response: Response<Void>
                                            ) {
                                                Log.d("ROLLCALL_DELETE", "점호 삭제 성공")
                                                Log.d(
                                                    "ROLLCALL_DELETE",
                                                    "response : " + response.body().toString()
                                                )

                                                Toast.makeText(
                                                    activity,
                                                    "점호가 취소되었습니다.",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                loadCalendarData(
                                                    token,
                                                    date.year.toString(),
                                                    date.month.toString(),
                                                    binding.calendar
                                                )
                                                dialog.cancel()
                                            }

                                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                                Log.e("ROLLCALL_DELETE", "점호 삭제 실패")
                                                Log.e("ROLLCALL_DELETE", t.message.toString())
                                            }
                                        })
                                    }
                            } else {
                                dialogLayout.findViewById<TextView>(R.id.btn_cancel).visibility =
                                    View.GONE
                            }
                        }

                        override fun onFailure(call: Call<RollcallGetDateResultDTO>, t: Throwable) {
                            Log.e("ROLLCALL_CHECK", "점호일 조회 실패")
                            Log.e("ROLLCALL_CHECK", t.message.toString())
                        }
                    })
            }
            // 점호일이 아닌 경우
            else {
                // 오늘 또는 오늘 이후 날짜만 점호 추가 가능
                if (date.isAfter(CalendarDay.today()) || date.equals(CalendarDay.today())) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("${date.date}")
                    builder.setMessage("점호를 추가하시겠습니까?")
                    builder.setPositiveButton(
                        "확인",
                        DialogInterface.OnClickListener { dialog, i ->
                            // 점호일 추가 API 연결
                            val rollcallInfo = RollcallCreateDTO(
                                date.date.toString() + "T23:59:59",
                                date.date.plusDays(1).toString() + "T00:30:00",
                                dormitory
                            )
                            RetrofitBuilder.rollcallApi.rollcallCreate(token, rollcallInfo)
                                .enqueue(object : Callback<RollcallCreateResultDTO> {
                                    override fun onResponse(
                                        call: Call<RollcallCreateResultDTO>,
                                        response: Response<RollcallCreateResultDTO>
                                    ) {
                                        Log.d("ROLLCALL_CREATE", "점호일 추가 성공")
                                        Log.d(
                                            "ROLLCALL_CREATE",
                                            "response : " + response.body().toString()
                                        )
                                        Log.d("ROLLCALL_CREATE", "code : " + response.code())
                                        Log.d("ROLLCALL_CREATE", "message : " + response.message())

                                        Toast.makeText(activity, "점호가 추가되었습니다.", Toast.LENGTH_SHORT)
                                            .show()
                                        loadCalendarData(
                                            token,
                                            binding.calendar.currentDate.year.toString(),
                                            binding.calendar.currentDate.month.toString(),
                                            binding.calendar
                                        )
                                        dialog.cancel()
                                    }

                                    override fun onFailure(
                                        call: Call<RollcallCreateResultDTO>,
                                        t: Throwable
                                    ) {
                                        Log.e("ROLLCALL_CREATE", "점호일 추가 실패")
                                        Log.e("ROLLCALL_CREATE", t.message.toString())
                                    }
                                })
                        }
                    )
                    builder.setNegativeButton(
                        "취소",
                        DialogInterface.OnClickListener { dialog, i ->
                            dialog.cancel()
                        }
                    )
                    builder.show()
                }
            }
        }

        return binding.root
    }

    // 캘린더 정보 불러오기 함수
    fun loadCalendarData(
        token: String,
        year: String,
        month: String,
        calendar: MaterialCalendarView
    ) {
        RetrofitBuilder.homeApi.homeInfo(token, year, month)
            .enqueue(object : Callback<HomeInfoResultDTO> {
                override fun onResponse(
                    call: Call<HomeInfoResultDTO>,
                    response: Response<HomeInfoResultDTO>
                ) {
                    Log.d("HOME_INFO", "캘린더 정보 조회 성공")
                    Log.d(
                        "HOME_INFO",
                        "roll-call days : " + response.body()?.rollcallDays.toString()
                    )
                    dayRollcall?.clear()
                    idRollcall?.clear()
                    for (rollcallDay in response.body()?.rollcallDays!!) {
                        dayRollcall?.add(rollcallDay.day)
                        idRollcall?.add(rollcallDay.id)
                    }

                    // 캘린더 정보 반영
                    val rollcallDecorator =
                        RollcallDecorator(this@HomeFragment, dayRollcall!!, month.toInt())
                    val todayDecorator = TodayDecorator(this@HomeFragment)
                    calendar.addDecorators(rollcallDecorator, todayDecorator)
                }

                override fun onFailure(call: Call<HomeInfoResultDTO>, t: Throwable) {
                    Log.d("HOME_INFO", "캘린더 정보 조회 실패")
                    Log.d("HOME_INFO", t.message.toString())
                }
            })
    }
}

// 디폴트 커스텀 함수
class DefaultDecorator(context: HomeFragment) : DayViewDecorator {
    val defaultDrawable = context.resources.getDrawable(R.drawable.style_home_cal_default)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(defaultDrawable)
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

// 점호일 커스텀 함수
class RollcallDecorator(context: HomeFragment, days: MutableList<Int>, month: Int) :
    DayViewDecorator {
    val rollcallDrawable = context.resources.getDrawable(R.drawable.style_home_cal_rollcall)
    val days = days
    val month = month

    override fun shouldDecorate(day: CalendarDay?): Boolean { // 커스텀 여부 반환
        return days.contains(day?.day) && day?.month == month
    }

    override fun decorate(view: DayViewFacade?) { // 커스텀 설정
        view?.setBackgroundDrawable(rollcallDrawable)
    }
}
