package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.data.NoticeCreateDTO
import kr.co.sujungvillage_admin.data.NoticeCreateResultDTO
import kr.co.sujungvillage_admin.data.NoticeDetailResultDTO
import kr.co.sujungvillage_admin.data.NoticeRequestResultDTO
import retrofit2.Call
import retrofit2.http.*

interface NoticeService {
    // 공지사항 리스트 조회
    @GET("/api/common/getAnnouncementList")
    fun noticeRequest(
        @Header("user_id") userId: String,
    ): Call<List<NoticeRequestResultDTO>>

    // 공지사항 상세 조회
    @GET("/api/common/getDetailedAnnouncement")
    fun noticeDetailRequest(
        @Header("user_id") userId: String,
        @Query("announcementId") noticeId: Long,
    ): Call<NoticeDetailResultDTO>

    // 공지사항 작성
    @POST("/api/admin/writeAnnouncement")
    fun noticeCreate(
        @Header("user_id") userId: String,
        @Body noticeInfo: NoticeCreateDTO,
    ): Call<NoticeCreateResultDTO>
}