package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.LoginDTO
import kr.co.sujungvillage_admin.data.LoginResultDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    // 관리자 로그인
    @POST("/api/admin/login")
    fun login(
        @Body loginInfo: LoginDTO
    ): Call<LoginResultDTO>
}
