package cl.jpinodev.virtualwalletapidb.data.model

import com.google.gson.annotations.SerializedName

data class Users(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val points: Int = 800 ,
    val roleId: Int = 1,
    val updatedAt: String = "2024-06-06T15:35:47.205Z",
    val createdAt: String = "2024-06-06T15:35:47.205Z"
)
