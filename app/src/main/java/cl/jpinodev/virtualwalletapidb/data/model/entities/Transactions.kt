package cl.jpinodev.virtualwalletapidb.data.model.entities

import android.accounts.Account
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Accounts::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Users::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["accountId"]), Index(value = ["userId"])]
)
data class Transactions(
    @PrimaryKey
    val id: Int,
    val amount: String,
    val concept: String,
    val date: String,
    val type: String,
    val accountId: Int,
    val userId: Int,
    @SerializedName("to_account_id")
    val toAccountId: Int,
    val createdAt: String,
    val updatedAt: String
)
