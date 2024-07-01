package cl.jpinodev.virtualwalletapidb.data.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class Accounts(
    @PrimaryKey
    val id: Int,
    val creationDate: String,
    val money: Int = 6000,
    val isBlocked: Boolean,
    val userId: Int,
    val updatedAt: String,
    val createdAt: String
)
