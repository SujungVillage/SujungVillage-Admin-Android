package kr.co.sujungvillage_admin.retrofit

import com.google.gson.GsonBuilder
import kr.co.sujungvillage_admin.BuildConfig.BASE_URL
import kr.co.sujungvillage_admin.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitBuilder {
    // 사용할 API 인터페이스 선언
    var communityApi: CommunityService
    var noticeApi: NoticeService
    var homeApi: HomeService
    var loginApi: LoginService
    var rollcallApi: RollcallService

    val gson = GsonBuilder().setLenient().create()

    init {
        // API 서버 연결
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        communityApi = retrofit.create(CommunityService::class.java)
        noticeApi = retrofit.create(NoticeService::class.java)
        homeApi = retrofit.create(HomeService::class.java)
        loginApi = retrofit.create(LoginService::class.java)
        rollcallApi = retrofit.create(RollcallService::class.java)
    }
}