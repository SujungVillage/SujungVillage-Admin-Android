package kr.co.sujungvillage_admin.retrofit // ktlint-disable package-name

import com.google.gson.GsonBuilder
import kr.co.sujungvillage_admin.BuildConfig.BASE_URL
import kr.co.sujungvillage_admin.api.* // ktlint-disable no-wildcard-imports
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    // 사용할 API 인터페이스 선언
    var communityApi: CommunityService
    var noticeApi: NoticeService
    var homeApi: HomeService
    var loginApi: LoginService
    var rollcallApi: RollcallService
    var rewardApi: RewardService
    var qnaApi: QnAService

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
        rewardApi = retrofit.create(RewardService::class.java)
        qnaApi = retrofit.create(QnAService::class.java)
    }
}
