package kr.co.sujungvillage_admin.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeInfoResultDTO(
    @SerializedName("adminInfo")
    val adminInfo: HomeAdminInfo,
    @SerializedName("rollcallDays")
    val rollcallDays: List<Int>,
): Serializable {}

data class HomeAdminInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("domitoryName")
    val dormitory: String,
    @SerializedName("description")
    val description: String,
): Serializable {}
