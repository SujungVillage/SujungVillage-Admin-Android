package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.LoginDTO
import kr.co.sujungvillage_admin.data.LoginResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityLoginBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 키보드 내리기
        binding.layoutIcon.setOnClickListener { this.hideKeyboard() }
        binding.layoutLogin.setOnClickListener { this.hideKeyboard() }

        // 자동 로그인 체크박스 초기화
        val shared = getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        if (shared.getBoolean("autoLogin", false)) {
            binding.checkAutoLogin.isChecked = true
            binding.editId.setText(shared.getString("loginID", ""))
            binding.editPassword.setText(shared.getString("loginPassword", ""))
        }

        // 로그인 버튼 연결
        binding.btnLogin.setOnClickListener {
            // 작성하지 않은 항목이 존재하는 경우
            if (binding.editId.text.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.editPassword.text.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // FCM 토큰 발급
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    // Log and toast
                    val msg = token.toString()
                    Log.d("FCM_TOKEN", msg)

                    // 관리자 로그인 API 연결
                    var loginInfo = LoginDTO(
                        binding.editId.text.toString(),
                        binding.editPassword.text.toString(),
                        msg
                    )
                    RetrofitBuilder.loginApi.login(loginInfo)
                        .enqueue(object : Callback<LoginResultDTO> {
                            override fun onResponse(
                                call: Call<LoginResultDTO>,
                                response: Response<LoginResultDTO>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("LOGIN", "로그인 성공")
                                    Log.d("LOGIN", "response : " + response.body().toString())

                                    // 로컬에 아이디, 토큰 저장
                                    val editor = shared.edit()
                                    editor.putString("id", binding.editId.text.toString())
                                    editor.putString("token", response.body()?.token)

                                    // 기본 정보를 기억하는 경우
                                    if (binding.checkAutoLogin.isChecked) {
                                        editor.putBoolean("autoLogin", true)
                                        editor.putString("loginID", binding.editId.text.toString())
                                        editor.putString(
                                            "loginPassword",
                                            binding.editPassword.text.toString()
                                        )
                                    }
                                    // 기본 정보를 기억하지 않는 경우
                                    else {
                                        editor.putBoolean("autoLogin", false)
                                    }
                                    editor.apply()

                                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this@LoginActivity, "로그인되었습니다.", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                } else {
                                    Log.d("LOGIN", "로그인 실패")
                                    Log.d("LOGIN", "response : " + response.body().toString())
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "아이디 또는 비밀번호를 다시 확인해주세요.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginResultDTO>, t: Throwable) {
                                Log.e("LOGIN", "로그인 실패")
                                Log.e("LOGIN", t.message.toString())
                                Toast.makeText(
                                    this@LoginActivity,
                                    "아이디 또는 비밀번호를 다시 확인해주세요.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            )
        }
    }
}
