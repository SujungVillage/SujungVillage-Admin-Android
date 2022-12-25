package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.NoticeCreateDTO
import kr.co.sujungvillage_admin.data.NoticeCreateResultDTO
import kr.co.sujungvillage_admin.data.NoticeDetailResultDTO
import kr.co.sujungvillage_admin.data.NoticeRequestResultDTO
import retrofit2.Call
import retrofit2.http.* // ktlint-disable no-wildcard-imports

interface NoticeService {
    // 공지사항 리스트 조회
    @GET("/api/common/announcement/getAnnouncementTitles")
    fun noticeRequest(
        @Header("jwt_token") token: String,
        @Query("dormitoryName") dormitory: String
    ): Call<List<NoticeRequestResultDTO>>

    // 공지사항 상세 조회
    @GET("/api/common/announcement/getAnnouncement")
    fun noticeDetailRequest(
        @Header("jwt_token") token: String,
        @Query("announcementId") noticeId: Long
    ): Call<NoticeDetailResultDTO>

    // 공지사항 작성
    @POST("/api/admin/announcement/writeAnnouncement")
    fun noticeCreate(
        @Header("jwt_token") token: String,
        @Body noticeInfo: NoticeCreateDTO
    ): Call<NoticeCreateResultDTO>
}
