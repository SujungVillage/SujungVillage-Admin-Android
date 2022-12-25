package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.RewardActivity.Companion.selecteduserId
import kr.co.sujungvillage_admin.adapter.RewardSelectedAdapter
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.base.showSnackbar
import kr.co.sujungvillage_admin.base.showToast
import kr.co.sujungvillage_admin.data.RewardCreateDTO
import kr.co.sujungvillage_admin.data.RewardCreateResultDTO
import kr.co.sujungvillage_admin.data.SelectedUser
import kr.co.sujungvillage_admin.databinding.ActivityRewardWriteBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardWriteActivity : AppCompatActivity() {
    lateinit var binding: ActivityRewardWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로컬에서 토큰 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        val token = shared?.getString("token", "error").toString()

        // 선택한 재사생 정보 불러오기
        val studentList = intent.getSerializableExtra("rewards") as MutableList<SelectedUser>
        var adapter = RewardSelectedAdapter()
        adapter.studentList = studentList
        binding.recyclerStudents.adapter = adapter
        binding.recyclerStudents.layoutManager = LinearLayoutManager(this)

        // 키보드 내리기
        binding.layout.setOnClickListener { this.hideKeyboard() }
        binding.linearReward.setOnClickListener { this.hideKeyboard() }
        binding.layoutToggle.setOnClickListener { this.hideKeyboard() }

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener {
            finish()
            val intent = Intent(this, RewardActivity::class.java)
            startActivity(intent)
        }

        // 등록 버튼 연결
        binding.btnRegister.setOnClickListener {
            if (binding.editPoint.text.isEmpty()) {
                showSnackbar(binding.root, "점수를 입력해주세요.")
                return@setOnClickListener
            }
            if (binding.editReason.text.isEmpty()) {
                showSnackbar(binding.root, "부여 사유를 입력해주세요.")
                return@setOnClickListener
            }

            // 상벌점 부여 API 연결
            var point = binding.editPoint.text.toString().toLong()
            if (binding.radioPenalty.isChecked) point *= -1
            val reason = binding.editReason.text.toString()
            RetrofitBuilder.rewardApi.rewardCreate(
                token,
                RewardCreateDTO(selecteduserId, point, reason)
            ).enqueue(object : Callback<List<RewardCreateResultDTO>> {
                override fun onResponse(
                    call: Call<List<RewardCreateResultDTO>>,
                    response: Response<List<RewardCreateResultDTO>>
                ) {
                    Log.d("REWARD_CREATE", "상벌점 부여 성공")

                    showToast("상벌점이 부여되었습니다.")
                    finish()
                }

                override fun onFailure(call: Call<List<RewardCreateResultDTO>>, t: Throwable) {
                    Log.e("REWARD_CREATE", "상벌점 부여 실패")

                    showToast("상벌점 부여 오류가 발생하였습니다.")
                }
            })
        }
    }
}
