package kr.co.sujungvillage_admin.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("password")
    val password: String,
): Serializable {}

data class LoginResultDTO(
    @SerializedName("jwtToken")
    val token: String,
): Serializable {}