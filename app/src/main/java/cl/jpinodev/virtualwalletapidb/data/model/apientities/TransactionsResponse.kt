package cl.jpinodev.virtualwalletapidb.data.model.apientities

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(
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