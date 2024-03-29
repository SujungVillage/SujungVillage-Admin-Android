package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.* // ktlint-disable no-wildcard-imports
import retrofit2.Call
import retrofit2.http.* // ktlint-disable no-wildcard-imports

interface RollcallService {
    // 점호일 조회
    @GET("/api/common/rollcall/getRollcallDateInfo")
    fun rollcallGetDate(
        @Header("jwt_token") token: String,
        @Query("rollcallDateId") id: Long
    ): Call<RollcallGetDateResultDTO>

    // 점호일 추가
    @POST("/api/admin/rollcall/addRollcallDate")
    fun rollcallCreate(
        @Header("jwt_token") token: String,
        @Body rollcallInfo: RollcallCreateDTO
    ): Call<RollcallCreateResultDTO>

    // 점호일 삭제
    @DELETE("/api/admin/rollcall/deleteRollcallDate")
    fun rollcallDelete(
        @Header("jwt_token") token: String,
        @Query("rollcallDateId") rollcallId: Long
    ): Call<Void>

    // 점호 신청 리스트 조회
    @GET("/api/admin/rollcall/getRollcallList")
    fun rollcallGetList(
        @Header("jwt_token") token: String,
        @Query("date") date: String,
        @Query("state") state: String
    ): Call<List<RollcallGetListResultDTO>>

    // 점호 상태 변경
    @PATCH("/api/admin/rollcall/changeRollcallState")
    fun rollcallChange(
        @Header("jwt_token") token: String,
        @Body rollcallInfo: RollcallChangeDTO
    ): Call<Void>

    // 대기 중인 점호 신청 리스트 조회
    @GET("/api/admin/rollcall/getWaitingRollcallList")
    fun rollcallGetWaitingList(
        @Header("jwt_token") token: String
    ): Call<List<RollcallGetListResultDTO>>
}
