package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.retrofit.HomeInfoResultDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HomeService {
    // 관리자 홈 화면 정보 조회
    @GET("/api/admin/home_info")
    fun homeInfo(
        @Header("user_id") userId: String,
        @Query("year") year: String,
        @Query("month") month: String,
    ): Call<HomeInfoResultDTO>
}