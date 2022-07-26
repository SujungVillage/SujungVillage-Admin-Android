package kr.co.sujungvillage_admin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kr.co.sujungvillage_admin.R
import kr.co.sujungvillage_admin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // lottie 이미지 회전
        binding.imgWave.rotationX = 180f

        // 알림 버튼 연결

        // 홈화면 주요 기능 버튼 연결
        // 1. 공지사항 버튼 연결
        // 2. 상벌점 관리 버튼 연결
        // 3. 점호 확인 버튼 연결
        // 4. 재사생 관리 버튼 연결

        return binding.root
    }
}