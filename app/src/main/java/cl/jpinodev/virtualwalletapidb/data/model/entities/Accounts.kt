package cl.jpinodev.virtualwalletapidb.data.model.entities

data class Accounts(
    val id: Int,
    val creationDate: String,
    val money: Int = 6000,
    val isBlocked: Boolean,
    val userId: Int,
    val updatedAt: String,
    val createdAt: String
)