package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.RewardActivity.Companion.selectedStudent
import kr.co.sujungvillage_admin.RewardActivity.Companion.selecteduserId
import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import kr.co.sujungvillage_admin.data.SelectedUser
import kr.co.sujungvillage_admin.databinding.ListitemRewardBinding

class RewardAdapter : RecyclerView.Adapter<RewardHolder>() {
    var studentList = mutableListOf<ResidentRequestResultDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardHolder {
        val binding =
            ListitemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RewardHolder(binding)
    }

    override fun onBindViewHolder(holder: RewardHolder, position: Int) {
        val student = studentList.get(position)
        holder.setStudent(student)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}

class RewardHolder(val binding: ListitemRewardBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setStudent(student: ResidentRequestResultDTO) {
        binding.userName.text = "${student.name}"
        binding.userId.text = "${student.userId}"
        binding.userDormitory.text = "${student.dormitoryName} ${student.detailedAddress}"
        // 선택한 학생인 경우
        if (selecteduserId.contains(student.userId)) binding.layout.setBackgroundResource(R.drawable.style_reward_item_selected)
        else binding.layout.setBackgroundResource(R.drawable.style_reward_item)

        // 학생을 선택한 경우
        binding.root.setOnClickListener {
            // 이미 선택한 학생인 경우
            if (selecteduserId.contains(student.userId)) {
                selectedStudent.remove(selectedStudent.get(selecteduserId.indexOf(student.userId)))
                selecteduserId.remove(student.userId)
                binding.layout.setBackgroundResource(R.drawable.style_reward_item)
            }
            // 선택되지 않은 학생인 경우
            else {
                selectedStudent.add(
                    SelectedUser(
                        student.name,
                        student.userId,
                        "${student.dormitoryName} ${student.detailedAddress}"
                    )
                )
                selecteduserId.add(student.userId)
                binding.layout.setBackgroundResource(R.drawable.style_reward_item_selected)
            }
        }
    }
}
