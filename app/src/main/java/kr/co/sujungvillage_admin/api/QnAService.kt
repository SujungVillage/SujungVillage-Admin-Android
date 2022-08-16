package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.data.FaqGetResultDTO
import kr.co.sujungvillage_admin.data.MyqDetailGetResultDTO
import kr.co.sujungvillage_admin.data.QuestionGetResultDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QnAService {
    // FAQ 질문 리스트 조회
    @GET("/api/common/qna/getAllFaq")
    fun faqGet(
        @Header("jwt_token") token: String,
    ): Call<List<FaqGetResultDTO>>

    // 모든 질문 조회
    @GET("/api/admin/qna/getAllQuestions")
    fun questionGet(
        @Header("jwt_token") token: String,
    ): Call<List<QuestionGetResultDTO>>

    // QnA 상세 조회
    @GET("/api/common/qna/getQna")
    fun questionDetailGet(
        @Header("jwt_token") token: String,
        @Query("questionId") questionId: Long,
    ): Call<MyqDetailGetResultDTO>
}