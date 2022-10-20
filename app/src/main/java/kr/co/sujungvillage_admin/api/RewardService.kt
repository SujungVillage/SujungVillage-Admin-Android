package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import kr.co.sujungvillage_admin.data.RewardCreateDTO
import kr.co.sujungvillage_admin.data.RewardCreateResultDTO
import retrofit2.Call
import retrofit2.http.*

interface RewardService {
    // 학생 리스트 조회
    @GET("/api/admin/getResidentList")
    fun studentRequest(
        @Header("jwt_token") token: String,
        @Query("dormitoryName") dormitoryName: String
    ): Call<List<ResidentRequestResultDTO>>

    // 상벌점 부여
    @POST("/api/admin/lmp/addLMP")
    fun rewardCreate(
        @Header("jwt_token") token: String,
        @Body() rewardInfo: RewardCreateDTO,
    ): Call<List<RewardCreateResultDTO>>
}