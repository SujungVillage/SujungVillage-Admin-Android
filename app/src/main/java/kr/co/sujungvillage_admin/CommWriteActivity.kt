package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kr.co.sujungvillage_admin.data.CommWriteDTO
import kr.co.sujungvillage_admin.data.CommWriteResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityCommWriteBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommWriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityCommWriteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val studentNum="20180001"

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener{ finish() }

        //등록 버튼
        binding.btnUpload.setOnClickListener{

            //버튼 클릭시 제목과 내용 받아오기.
            val title= binding.editTitle.text.toString().trim()//앞뒤 공백 제거
            val content=binding.editContent.text.toString().trim()//앞뒤 공백 제거

            //내용이 null인지 확인 필요
            if(title.isEmpty()){
                if(content.isEmpty()){
                    Toast.makeText(this,"제목과 내용을 입력하세요. ${title}, ${content}", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"제목을 입력하세요. ${title}, ${content}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                if(content.isEmpty()){
                    Toast.makeText(this,"내용을 입력하세요. ${title}, ${content}", Toast.LENGTH_SHORT).show()
                }
                else{
                    //서버에 보내기
                    val commWriteInfo= CommWriteDTO(title,content)
                    RetrofitBuilder.communityApi.commWrite(studentNum,commWriteInfo).enqueue(object:
                        Callback<CommWriteResultDTO> {
                        override fun onResponse(call: Call<CommWriteResultDTO>, response: Response<CommWriteResultDTO>) {
                            Log.d("COMM_WRITE", response.body().toString())
                            finish()
                        }

                        override fun onFailure(call: Call<CommWriteResultDTO>, t: Throwable) {
                            Log.d("COMMW_RITE",t.message.toString())
                        }
                    })
                }
            }
        }
    }
}