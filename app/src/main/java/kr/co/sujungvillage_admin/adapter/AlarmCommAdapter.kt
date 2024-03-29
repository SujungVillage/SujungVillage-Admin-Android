package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.data.Alarm
import kr.co.sujungvillage_admin.databinding.ListitemAlarmCommBinding

class AlarmCommAdapter : RecyclerView.Adapter<AlarmCommHolder>() {
    var alarmList = mutableListOf<Alarm>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmCommHolder {
        val binding =
            ListitemAlarmCommBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmCommHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmCommHolder, position: Int) {
        val alarm = alarmList.get(position)
        holder.setAlarm(alarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}

class AlarmCommHolder(val binding: ListitemAlarmCommBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun setAlarm(alarm: Alarm) {
        binding.textTitle.text = alarm.title
        binding.textContent.text = alarm.content
        binding.textDate.text = alarm.date

        // 아직 읽지 않은 알림인 경우
        if (!alarm.isRead) {
            binding.layout.setBackgroundResource(R.drawable.style_alarm_unread)
        }
    }
}
