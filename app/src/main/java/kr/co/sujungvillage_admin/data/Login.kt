package kr.co.sujungvillage_admin.data // ktlint-disable package-name

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("fcm_token")
    val fcm: String
) : Serializable

data class LoginResultDTO(
    @SerializedName("jwtToken")
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String
) : Serializable

data class RequestTokenRefreshDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("refresh_token")
    val refreshToken: String
) : Serializable

data class ResponseTokenRefreshDto(
    val jwtToken: String
) : Serializable
