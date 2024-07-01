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
    private val _transactionsLD = MutableLiveData<Result<List<Transactions>?>>()
    val transactionsLD: LiveData<Result<List<Transactions>?>> = _transactionsLD

    fun getTransactions(token: String, accountId: Int) {
        viewModelScope.launch {
            try {
                // pedimos las transacciones a la api
                val response = transactionUseCase.getTransactions(token)
                if (response.isSuccessful) {
                    val transactions = response.body()?.data
                    _transactionsLD.postValue(Result.success(transactions))
                    //guardamos en db
                    response.body()?.data?.let { transactionsList ->
                        saveTransactionsOnDb(transactionsList)
                    }
                } else {
                    val transactionsDb = transactionUseCase.getTransactionsFromDb(accountId)
                    _transactionsLD.postValue(transactionsDb)
                }
            } catch (e: Exception) {
                val transactionsDb = transactionUseCase.getTransactionsFromDb(accountId)
                _transactionsLD.postValue(transactionsDb)
            }
        }
    }

    private fun saveTransactionsOnDb(transactions: List<Transactions>) {
        viewModelScope.launch {
            transactionUseCase.saveTransactionsOnDb(transactions)
        }
    }
}
