package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.sujungvillage_admin.databinding.ActivityRollCallBinding

class RollCallActivity : AppCompatActivity() {
    val binding by lazy { ActivityRollCallBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textCurrentRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
        setCurrentRollCallFragment()

        binding.textCurrentRollcall.setOnClickListener {
            binding.textCurrentRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
            binding.textAllRollcall.setBackgroundResource(0)
            setCurrentRollCallFragment()
        }
        binding.textAllRollcall.setOnClickListener {
            binding.textAllRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
            binding.textCurrentRollcall.setBackgroundResource(0)
            setAllRollCallFragment()
        }
    }

    private fun setAllRollCallFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_rollcall, AllRollCall())
            .commit()
    }

    private fun setCurrentRollCallFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.frame_rollcall, CurrentRollCall())
            .commit()
    }
}
