package cl.jpinodev.virtualwalletapidb.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class Users(
    @PrimaryKey
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String ,
    val password: String,
    val points: Int = 800,
    val roleId: Int = 1,
    val updatedAt: String = "2024-06-06T15:35:47.205Z",
    val createdAt: String = "2024-06-06T15:35:47.205Z"
)
