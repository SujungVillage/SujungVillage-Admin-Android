package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kr.co.sujungvillage_admin.databinding.ActivityMainBinding
import kr.co.sujungvillage_admin.databinding.ActivityRewardBinding

class RewardActivity : AppCompatActivity() {

    val binding by lazy { ActivityRewardBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        val items = resources.getStringArray(R.array.comm_dormitory)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        binding.spinner.adapter = myAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                when (position) {
                    0 -> {
                        //전체
                    }

                    1 -> {
                        //성미료
                    }
                    2 -> {
                        //성미관
                    }
                    3 -> {
                        //풍림
                    }
                    4 -> {
                        //엠시티
                    }
                    5 -> {
                        //그레이스
                    }
                    6 -> {
                        //이율
                    }
                    7 -> {
                        //장수
                    }
                    else -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}