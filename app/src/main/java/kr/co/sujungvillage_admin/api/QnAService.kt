package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.* // ktlint-disable no-wildcard-imports
import retrofit2.Call
import retrofit2.http.* // ktlint-disable no-wildcard-imports

interface QnAService {

    // QNA 답변 작성
    @POST("/api/admin/qna/writeAnswer")
    fun qnaAnswer(
        @Header("jwt_token") token: String,
        @Body qnaAnswerInfo: QnaAnswerDTO
    ): Call<QnaAnswerResultDTO>

    // FAQ 작성
    @POST("/api/admin/qna/writeFaq")
    fun faqWrite(
        @Header("jwt_token") token: String,
        @Body faqWriteInfo: FaqWriteRequestDTO
    ): Call<FaqWriteResultDTO>

    // FAQ 질문 리스트 조회
    @GET("/api/common/qna/getAllFaq")
    fun faqGet(
        @Header("jwt_token") token: String
    ): Call<List<FaqGetResultDTO>>

    // 모든 질문 조회
    @GET("/api/admin/qna/getAllQuestions")
    fun questionGet(
        @Header("jwt_token") token: String
    ): Call<List<QuestionGetResultDTO>>

    // QnA 상세 조회
    @GET("/api/common/qna/getQna")
    fun questionDetailGet(
        @Header("jwt_token") token: String,
        @Query("questionId") questionId: Long
    ): Call<MyqDetailGetResultDTO>
}
