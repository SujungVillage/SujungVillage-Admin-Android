package kr.co.sujungvillage_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityRewardBinding
import kr.co.sujungvillage_admin.databinding.ListitemRewardBinding

class RewardAdapter : RecyclerView.Adapter<RewardHolder>() {

    val studentList = mutableListOf<ResidentRequestResultDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardHolder {
        val binding = ListitemRewardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun setStudent(student: ResidentRequestResultDTO ) {
        binding.userName.text = "${student.name}"
        binding.userId.text = "${student.userId}"
        binding.userDormitory.text = "${student.dormitoryName}, ${student.detailedAddress}"
    }

}
