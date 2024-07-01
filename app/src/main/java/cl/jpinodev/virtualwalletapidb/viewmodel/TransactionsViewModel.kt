package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import cl.jpinodev.virtualwalletapidb.domain.TransactionsUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class TransactionsViewModel(private val transactionUseCase: TransactionsUseCase) : ViewModel() {
    private val _transactionsLD = MutableLiveData<Result<Response<TransactionResponse>>>()
    val transactionsLD: LiveData<Result<Response<TransactionResponse>>> = _transactionsLD

    fun getTransactions(token: String) {
        viewModelScope.launch {
            try {
                val response =
                    transactionUseCase.getTransactions(token) // pedimos las transacciones a la api
                if (response.isSuccessful) {

                    _transactionsLD.postValue(Result.success(response))
                    //guardamos en db
                    response.body()?.data?.let { transactions ->
                        saveTransactionsOnDb(transactions)
                    }
                } else {
                    _transactionsLD.postValue(
                        Result.failure(
                            Exception(
                                response.errorBody()?.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                _transactionsLD.postValue(Result.failure(e))
            }
        }
    }

    private fun saveTransactionsOnDb(transactions: List<Transactions>) {
        viewModelScope.launch {
            transactionUseCase.saveTransactionsOnDb(transactions)
        }
    }
}
