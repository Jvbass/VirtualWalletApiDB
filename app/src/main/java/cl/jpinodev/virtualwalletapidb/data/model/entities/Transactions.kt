package cl.jpinodev.virtualwalletapidb.data.model.entities

data class Transactions(
    val id: Int,
    val creationDate: String,
    val money: String,
    val isBlocked: Boolean,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String
)