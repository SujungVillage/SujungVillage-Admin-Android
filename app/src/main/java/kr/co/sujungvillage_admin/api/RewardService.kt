package kr.co.sujungvillage_admin.api

import kr.co.sujungvillage_admin.data.ResidentRequestResultDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RewardService {

    //학생 리스트 조회
    @GET("/api/admin/getResidentList")
    fun  studentRequest(
        @Header("jwt_token") token: String,
        @Query("dormitoryName") dormitoryName: String
    ): Call<List<ResidentRequestResultDTO>>

}