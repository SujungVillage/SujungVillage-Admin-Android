package kr.co.sujungvillage_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.data.SelectedUser
import kr.co.sujungvillage_admin.databinding.ListitemRewardWriteBinding

class RewardSelectedAdapter : RecyclerView.Adapter<RewardSelectedHolder>() {
    var studentList = mutableListOf<SelectedUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardSelectedHolder {
        val binding = ListitemRewardWriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RewardSelectedHolder(binding)
    }

    override fun onBindViewHolder(holder: RewardSelectedHolder, position: Int) {
        val student = studentList.get(position)
        holder.setStudent(student)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}

class RewardSelectedHolder(val binding: ListitemRewardWriteBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setStudent(student: SelectedUser) {
        binding.textName.text = student.name
        binding.textId.text = student.userId
        binding.textDormitory.text = student.dormitoryName
    }
}