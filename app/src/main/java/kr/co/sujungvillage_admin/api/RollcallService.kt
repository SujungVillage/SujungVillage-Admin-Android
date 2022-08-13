package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.data.RollcallCreateDTO
import kr.co.sujungvillage_admin.data.RollcallCreateResultDTO
import kr.co.sujungvillage_admin.data.RollcallGetDateResultDTO
import retrofit2.Call
import retrofit2.http.*

interface RollcallService {
    // 점호일 조회
    @GET("/api/common/rollcall/getRollcallDateInfo")
    fun rollcallGetDate(
        @Header("jwt_token") token: String,
        @Query("rollcallDateId") id: Long,
    ): Call<RollcallGetDateResultDTO>

    // 점호일 추가
    @POST("/api/admin/rollcall/addRollcallDate")
    fun rollcallCreate(
        @Header("jwt_token") token: String,
        @Body rollcallInfo: RollcallCreateDTO,
    ): Call<RollcallCreateResultDTO>
}