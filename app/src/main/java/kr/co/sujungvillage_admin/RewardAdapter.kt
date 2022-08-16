package kr.co.sujungvillage_admin

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import kr.co.sujungvillage_admin.databinding.ListitemRewardBinding

class RewardAdapter() : RecyclerView.Adapter<RewardHolder>() {

    var studentList = mutableListOf<ResidentRequestResultDTO>()

    private var datas = mutableListOf<RewardData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardHolder {
        val binding = ListitemRewardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RewardHolder(binding)
    }


    override fun onBindViewHolder(holder: RewardHolder, position: Int) {
        val student = studentList.get(position)
        holder.setReward(student)

    }

    override fun getItemCount(): Int {
        return  studentList.size
    }



}

class RewardHolder(val binding: ListitemRewardBinding): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun setReward(student: ResidentRequestResultDTO) {
        binding.userId.text = "${student.userId}"
        binding.userName.text = student.name
        binding.userDormitory.text = "${student.dormitoryName} + ${student.detailedAddress}"
    }
}