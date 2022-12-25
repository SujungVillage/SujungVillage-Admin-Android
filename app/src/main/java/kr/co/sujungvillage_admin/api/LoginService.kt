package kr.co.sujungvillage_admin.api // ktlint-disable package-name

import kr.co.sujungvillage_admin.data.LoginDTO
import kr.co.sujungvillage_admin.data.LoginResultDTO
import kr.co.sujungvillage_admin.data.RequestTokenRefreshDto
import kr.co.sujungvillage_admin.data.ResponseTokenRefreshDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    // 관리자 로그인
    @POST("/api/admin/login")
    fun login(
        @Body loginInfo: LoginDTO
    ): Call<LoginResultDTO>

    // 토큰 리프레쉬
    @POST("/api/common/refresh")
    fun tokenRefresh(
        @Body requestTokenRefreshDto: RequestTokenRefreshDto
    ): Call<ResponseTokenRefreshDto>
}
