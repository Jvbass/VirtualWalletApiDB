package cl.jpinodev.virtualwalletapidb.data.model

data class Accounts(
    val id: Int,
    val creationDate: String,
    val money: Int,
    val isBlocked: Boolean,
    val userId: Int,
    val updatedAt: String,
    val createdAt: String
)