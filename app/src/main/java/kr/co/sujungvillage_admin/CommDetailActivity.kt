package kr.co.sujungvillage_admin // ktlint-disable package-name

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.sujungvillage_admin.adapter.CommDetailAdapter
import kr.co.sujungvillage_admin.base.hideKeyboard
import kr.co.sujungvillage_admin.data.CommDetailCommentWriteResultDTO
import kr.co.sujungvillage_admin.data.CommDetailCommentsRequest
import kr.co.sujungvillage_admin.data.CommDetailCommentsWriteDTO
import kr.co.sujungvillage_admin.data.CommDetailResultDTO
import kr.co.sujungvillage_admin.databinding.ActivityCommDetailBinding
import kr.co.sujungvillage_admin.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommDetailActivity : AppCompatActivity() {
    companion object {
        var adminNum = ""
        var token = ""
        var commentIndex: MutableList<String>? = null
        var postId = 0L
        var postWriterId = 0
    }

    val binding by lazy { ActivityCommDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 재사생 학번 불러오기
        val shared = this.getSharedPreferences("SujungVillage_Admin", Context.MODE_PRIVATE)
        adminNum = shared?.getString("id", "error").toString()
        token = shared?.getString("token", "error").toString()

        // 이전 페이지 CommFragment 에서 postId 전달 받기
        postId = intent.getLongExtra("postId", -1)
        val dormitory = intent.getStringExtra("dormitory")

        // 키보드 내리기
        binding.layoutToolbar.setOnClickListener { this.hideKeyboard() }
        binding.linearPost.setOnClickListener { this.hideKeyboard() }
        binding.linearButton.setOnClickListener { this.hideKeyboard() }
        binding.linearComment.setOnClickListener { this.hideKeyboard() }
        binding.recyclerComment.setOnClickListener { this.hideKeyboard() }
        // 리프레시
        binding.swipe.setOnRefreshListener {
            refresh(token, adminNum, postId)
            Log.d("REFRESH", "$adminNum,$postId")
            binding.swipe.isRefreshing = false
        }

        // 툴바 타이틀 설정
        binding.textToolbarTitle.text = "$dormitory 게시판"
        // 뒤로가기 버튼 연결
        binding.btnBack.setOnClickListener { finish() }

        // Api 연결
        refresh(token, adminNum, postId)
        // 글 삭제 버튼 연결
        binding.btnDelete.setOnClickListener {
            // 경고창 띄우기
            val builder = AlertDialog.Builder(this)
            builder.setTitle("정말 삭제하시겠습니까?")
                .setPositiveButton(
                    "확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        RetrofitBuilder.communityApi.commDelete(token, postId)
                            .enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    Log.d("COMM_DELETE", response.body().toString())

                                    finish()
                                }

                                override fun onFailure(call: Call<Void>, t: Throwable) {
                                    Log.e("COMM_DELETE", t.message.toString())
                                }
                            })
                    }
                )
                .setNegativeButton(
                    "취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        Log.d("COMM_DELETE", "글 삭제 취소")
                    }
                )
            builder.show()
        }

        // 댓글 전송 버튼 클릭시
        binding.btnCommentSubmit.setOnClickListener {
            val comment = binding.editComment.text.toString().trim()
            binding.editComment.text.clear() // edittext 지우기
            hideKeyboard()

            if (comment.isEmpty()) {
                Toast.makeText(this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else { // POST
                val commCommentInfo = CommDetailCommentsWriteDTO(postId, comment)
                RetrofitBuilder.communityApi.commComment(token, commCommentInfo)
                    .enqueue(object : Callback<CommDetailCommentWriteResultDTO> {
                        override fun onResponse(
                            call: Call<CommDetailCommentWriteResultDTO>,
                            response: Response<CommDetailCommentWriteResultDTO>
                        ) {
                            Log.d("COMM_COMMENT", response.body().toString())
                            refresh(token, adminNum, postId)
                        }

                        override fun onFailure(
                            call: Call<CommDetailCommentWriteResultDTO>,
                            t: Throwable
                        ) {
                            Log.d("COMM_COMMENT", t.message.toString())
                        }
                    })
            }
        }
    }

    private fun refresh(token: String, studentNum: String, postId: Long) {
        RetrofitBuilder.communityApi.commDetail(token, postId)
            .enqueue(object : Callback<CommDetailResultDTO> {
                override fun onResponse(
                    call: Call<CommDetailResultDTO>,
                    response: Response<CommDetailResultDTO>
                ) {
                    Log.d("COMM_DETAIL", response.body().toString())
                    if (response.isSuccessful) {
                        commentIndex = mutableListOf()

                        binding.textTitle.text = response.body()?.title
                        binding.textCalDate.text = "${response.body()?.regDate?.subSequence(0, 4)}/${response.body()?.regDate?.subSequence(5, 7)}/${response.body()?.regDate?.subSequence(8, 10)} ${response.body()?.regDate?.subSequence(11, 13)}:${response.body()?.regDate?.subSequence(14, 16)}"
                        binding.textContent.text = response.body()?.content

                        // 관리자이면 관리자 마크 보이게
                        try {
                            postWriterId = response.body()?.writerId.toString().toInt()
                            if (response.body()?.writerId.toString()
                                    .toInt() >= 99990000
                            ) { // 관리자인 경우
                                binding.textAdmin.visibility = View.VISIBLE
                            }
                        } catch (e: NumberFormatException) { }

                        // 글 작성자 id 와 studentNum이 같으면 삭제 버튼 보이게
                        if (studentNum == response.body()?.writerId) {
                            binding.btnDelete.visibility = View.VISIBLE
                        }

                        // 어댑터 연결
                        val commentList: MutableList<CommDetailCommentsRequest> = mutableListOf()
                        var commentCount = 0
                        for (info in response.body()?.comments!!) {
                            commentCount++
                            if (!commentIndex?.contains(info.writerId)!!) commentIndex!!.add(info.writerId)
                            val comment = CommDetailCommentsRequest(
                                info.id,
                                info.postId,
                                info.writerId,
                                info.content,
                                info.regDate,
                                info.modDate
                            )
                            commentList.add(comment)
                        }
                        val adapter = CommDetailAdapter(this@CommDetailActivity)
                        adapter.commDetailList = commentList
                        binding.recyclerComment.adapter = adapter
                        binding.recyclerComment.layoutManager =
                            LinearLayoutManager(this@CommDetailActivity)

                        binding.textCommentCount.text = commentCount.toString()
                    } else {
                        val builder = AlertDialog.Builder(this@CommDetailActivity)
                        builder.setTitle("글이 존재하지 않습니다.")
                            .setPositiveButton(
                                "확인",
                                DialogInterface.OnClickListener { dialog, id ->
                                    Log.d("COMM_DETAIL", "글이 존재하지 않음")
                                    finish()
                                }
                            )
                        builder.show()
                    }
                }

                override fun onFailure(call: Call<CommDetailResultDTO>, t: Throwable) {
                    Log.e("COMM_DETAIL", t.message.toString())
                }
            })
    }
}
