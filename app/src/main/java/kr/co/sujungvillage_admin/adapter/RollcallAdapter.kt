package kr.co.sujungvillage_admin.adapter // ktlint-disable package-name

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.sujungvillage_admin.CurrentRollCall.Companion.selectedRollcall
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.base.toBitmap
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.ListitemRollcallBinding

class RollcallAdapter : RecyclerView.Adapter<RollcallHolder>() {
    var rollcallList = mutableListOf<RollcallGetListResultDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollcallHolder {
        val binding =
            ListitemRollcallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RollcallHolder(binding)
    }

    override fun onBindViewHolder(holder: RollcallHolder, position: Int) {
        val rollcall = rollcallList.get(position)
        holder.setRollcall(rollcall)
    }

    override fun getItemCount(): Int {
        return rollcallList.size
    }
}

class RollcallHolder(val binding: ListitemRollcallBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setRollcall(rollcall: RollcallGetListResultDTO) {
        binding.imgRollcall.setImageBitmap(rollcall.image.toBitmap())
        binding.textName.text = "이름 : ${rollcall.userName}"
        binding.textStudentNum.text = "학번 : ${rollcall.userId}"
        binding.textDormitory.text = "기숙사 : ${rollcall.dormitory} ${rollcall.address}"
        binding.textLocation.text = "위치 : ${rollcall.location}"

        // 점호를 선택한 경우
        binding.root.setOnClickListener {
            // 이미 선택한 점호인 경우
            if (selectedRollcall.contains(rollcall.id)) {
                selectedRollcall.remove(rollcall.id)
                binding.layout.setBackgroundResource(R.drawable.style_rollcall_detail_border)
            }
            // 선택되지 않은 점호인 경우
            else {
                selectedRollcall.add(rollcall.id)
                binding.layout.setBackgroundResource(R.drawable.style_rollcall_detail_select)
            }
        }
    }
}
