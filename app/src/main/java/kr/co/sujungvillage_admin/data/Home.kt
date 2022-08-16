package kr.co.sujungvillage_admin.retrofit

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeInfoResultDTO(
    @SerializedName("adminInfoDTO")
    val adminInfo: HomeAdminInfo,
    @SerializedName("rollcallDays")
    val rollcallDays: List<HomeDay>,
): Serializable {}

data class HomeAdminInfo(
    @SerializedName("name")
    val name: String,
    @SerializedName("dormitoryName")
    val dormitory: String,
    @SerializedName("detailedAddress")
    val description: String,
): Serializable {}

data class HomeDay(
    @SerializedName("id")
    val id: Long,
    @SerializedName("day")
    val day: Int,
): Serializable {}