package cl.jpinodev.virtualwalletapidb.data.model.apientities

data class LoginRequest(
    val email: String,
    val password: String
)