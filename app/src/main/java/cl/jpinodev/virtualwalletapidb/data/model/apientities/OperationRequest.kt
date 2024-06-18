package cl.jpinodev.virtualwalletapidb.data.model.apientities

data class OperationRequest(
    val type: String,
    val concept: String,
    val amount: Int
)
