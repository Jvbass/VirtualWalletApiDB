package cl.jpinodev.virtualwalletapidb.data.model.apientities

import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions

data class TransactionResponse(
    val previousPage: String?,
    val nextPage: String?,
    val data: List<Transactions>
)