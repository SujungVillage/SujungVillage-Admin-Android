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
    @SerializedName("startDateTime")
    val start: String,
    @SerializedName("endDateTime")
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

data class RollcallGetListResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("dormitoryName")
    val dormitory: String,
    @SerializedName("detailedAddress")
    val address: String,
    @SerializedName("image")
    val image: ByteArray,
    @SerializedName("location")
    val location: String,
    @SerializedName("rollcallDateTime")
    val date: String,
    @SerializedName("state")
    val state: String,
): Serializable {}

data class RollcallChangeDTO(
    @SerializedName("rollcallIds")
    val id: List<Long>,
    @SerializedName("state")
    val state: String,
): Serializable {}

