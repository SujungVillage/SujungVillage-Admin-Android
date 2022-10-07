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
) : Serializable {}
