package cl.jpinodev.virtualwalletapidb.data.model.apientities

data class TransactionResponse(
    val previousPage: String?,
    val nextPage: String?,
    val data: List<TransactionsResponse>
)