package cl.jpinodev.virtualwalletapidb.data.model.apientities

data class AccountRequest(
    val creationDate: String,
    val money: Int,
    val isBlocked: Boolean,
    val userId: Int
)
