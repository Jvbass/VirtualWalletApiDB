package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.domain.TransactionsUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class TransactionsViewModel(private val useCase: TransactionsUseCase) : ViewModel() {
    private val _transactionsLD = MutableLiveData<Result<Response<TransactionResponse>>>()
    val transactionsLD: LiveData<Result<Response<TransactionResponse>>> = _transactionsLD
    fun getTransactions(token: String) {
        viewModelScope.launch {
            try {
                val response = useCase.getTransactions(token) // aca pedimos las transacciones a la api
                if (response.isSuccessful) {
                    _transactionsLD.postValue(Result.success(response))
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
}
