package kr.co.sujungvillage_admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.sujungvillage_admin.CommWriteActivity
import kr.co.sujungvillage_admin.NoticeActivity
import kr.co.sujungvillage_admin.databinding.FragmentCommunityBinding

class CommFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentCommunityBinding.inflate(inflater, container, false)

        // 글 작성 버튼 연결
        binding.imageButton3.setOnClickListener {
            var intent = Intent(this.activity, CommWriteActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}