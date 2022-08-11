package kr.co.sujungvillage_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.adapter.CommDetailAdapter
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.CommDetailCommentWriteResultDTO
import kr.co.sujungvillage_admin.data.CommDetailCommentsRequest
import kr.co.sujungvillage_admin.data.CommDetailCommentsWriteDTO
import kr.co.sujungvillage_admin.data.CommDetailResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityCommDetailBinding
import kr.co.sujungvillage_admin.databinding.ActivityCommWriteBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommDetailActivity : AppCompatActivity() {
    val binding by lazy { ActivityCommDetailBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val studentNum="20180001"

        //CommFragment 에서 postId 전달 받기
        val postId=intent.getLongExtra("postId",-1)

        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener{ finish() }
        refresh(studentNum,postId)

        //댓글 전송 버튼 클릭시
        binding.btnCommentSubmit.setOnClickListener{
            val comment=binding.editComment.text.toString().trim()
            binding.editComment.text.clear()//edittext 지우기
            hideKeyboard()

            if(comment.isEmpty()){
                Toast.makeText(this,"댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            else{//POST
                val commCommentInfo= CommDetailCommentsWriteDTO(postId,comment)
                RetrofitBuilder.communityApi.commComment(studentNum,commCommentInfo).enqueue(object:
                    Callback<CommDetailCommentWriteResultDTO> {
                    override fun onResponse(call: Call<CommDetailCommentWriteResultDTO>, response: Response<CommDetailCommentWriteResultDTO>) {
                        Log.d("COMM_COMMENT",response.body().toString())
                        refresh(studentNum,postId)
                    }

                    override fun onFailure(call: Call<CommDetailCommentWriteResultDTO>, t: Throwable) {
                        Log.d("COMM_COMMENT",t.message.toString())
                    }
                })
            }
        }
    }
    private fun refresh(studentNum:String,postId:Long){
        RetrofitBuilder.communityApi.commDetail(studentNum,postId).enqueue(object: Callback<CommDetailResultDTO>{

            override fun onResponse(call: Call<CommDetailResultDTO>, response: Response<CommDetailResultDTO>) {
                Log.d("COMM_DETAIL",response.body().toString())
                binding.textTitle.text=response.body()?.title
                val hour=response.body()?.regDate?.subSequence(11,13).toString().toInt()
                var hourResult=hour.toString()
                if (hour<10){
                    hourResult="0${hour}"
                }
                val min=(response.body()?.regDate?.subSequence(14,16).toString().toInt())
                var minResult=min.toString()
                if(min<10){
                    minResult="0${min}"
                }
                binding.textCalDate.text="${response.body()?.regDate?.subSequence(0,4)}/${response.body()?.regDate?.subSequence(5, 7)}/${response.body()?.regDate?.subSequence(8, 10)} ${hourResult}:${minResult}"
                binding.textContent.text=response.body()?.content
                //글 작성자 id 와 studentNum이 같으면 삭제 버튼 보이게
                if(studentNum==response.body()?.writerId){
                    binding.btnDelete.visibility= View.VISIBLE
                }
                //어댑터 연결
                val commentList:MutableList<CommDetailCommentsRequest> = mutableListOf()
                var commentCount=0
                for(info in response.body()?.comments!!){
                    commentCount++
                    var comment=CommDetailCommentsRequest(info.id,info.writerId,info.content,info.regDate,info.modDate)
                    commentList.add(comment)
                }
                var adapter= CommDetailAdapter()
                adapter.commDetailList=commentList
                binding.recyclerComment.adapter=adapter
                binding.recyclerComment.layoutManager= LinearLayoutManager(this@CommDetailActivity)

                binding.textCommentCount.text=commentCount.toString()
            }

            override fun onFailure(call: Call<CommDetailResultDTO>, t: Throwable) {
                Log.d("COMM_DETAIL",t.message.toString())
            }
        })
    }
}