package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResidentRequestResultDTO(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("dormitoryName")
    val dormitoryName: String,
    @SerializedName("detailedAddress")
    val detailedAddress: String,
) : Serializable

data class SelectedUser(
    val name: String,
    val userId: String,
    val dormitoryName: String,
) : Serializable

data class RewardCreateDTO(
    @SerializedName("residentList")
    val residentIds: List<String>,
    @SerializedName("score")
    val score: Long,
    @SerializedName("reason")
    val reason: String,
) : Serializable

data class RewardCreateResultDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("score")
    val score: Long,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("regDate")
    val date: String,
) : Serializable