package cl.jpinodev.virtualwalletapidb.data.model

import com.google.gson.annotations.SerializedName

data class Users(
    val id: Int,
    @SerializedName("firstName")
    val first_name: String,
    @SerializedName("lastName")
    val last_name: String,
    val email: String,
    val password: String,
    val points: Int = 800 ,
    val roleId: Int = 1,
    val updatedAt: String = "2024-06-06T15:35:47.205Z",
    val createdAt: String = "2024-06-06T15:35:47.205Z"
)
