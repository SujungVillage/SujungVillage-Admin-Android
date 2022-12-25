package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.HomeInfoResultDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HomeService {
    // 관리자 홈 화면 정보 조회
    @GET("/api/admin/home/getInfo")
    fun homeInfo(
        @Header("jwt_token") token: String,
        @Query("year") year: String,
        @Query("month") month: String
    ): Call<HomeInfoResultDTO>
}
