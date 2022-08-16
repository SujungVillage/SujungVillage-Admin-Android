package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RollcallGetDateResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("start")
    val start: String,
    @SerializedName("end")
    val end : String,
    @SerializedName("dormitoryName")
    val dormitory: String,
): Serializable {}

data class RollcallCreateDTO(
    @SerializedName("date")
    val date: String,
    @SerializedName("startTime")
    val start: String,
    @SerializedName("endTime")
    val end: String,
    @SerializedName("dormitoryName")
    val dormitory: String,
): Serializable {}

data class RollcallCreateResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("start")
    val start: String,
    @SerializedName("end")
    val end: String,
    @SerializedName("dormitoryName")
    val dormitory: String,
): Serializable {}