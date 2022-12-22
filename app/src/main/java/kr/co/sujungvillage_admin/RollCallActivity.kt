package kr.co.sujungvillage_admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import kr.co.sujungvillage_admin.adapter.RollcallAdapter
import kr.co.sujungvillage_admin.data.RollcallChangeDTO
import kr.co.sujungvillage_admin.data.RollcallGetListResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityRollCallBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.Fragment

class RollCallActivity : AppCompatActivity() {
    val binding by lazy { ActivityRollCallBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textCurrentRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
        setCurrentRollCallFragment()

        binding.textCurrentRollcall.setOnClickListener{
            binding.textCurrentRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
            binding.textAllRollcall.setBackgroundResource(0)
            setCurrentRollCallFragment()
        }
        binding.textAllRollcall.setOnClickListener{
            binding.textAllRollcall.setBackgroundResource(R.drawable.style_rollcall_tab)
            binding.textCurrentRollcall.setBackgroundResource(0)
            setAllRollCallFragment()
        }

    }

    private fun setAllRollCallFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_rollcall,AllRollCall()).commit()
    }
    private fun setCurrentRollCallFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.frame_rollcall,CurrentRollCall()).commit()

    }
}