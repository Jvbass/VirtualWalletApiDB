package cl.jpinodev.virtualwalletapidb.data.model.apientities

data class SendMoneyRequest(
    val type: String,
    val concept: String,
    val amount: Int
)
