package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountsResponse
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class AccountsViewModel(private val accountUseCase: AccountsUseCase) : ViewModel() {
    private val _accountLD = MutableLiveData<Result<Response<AccountsResponse>>>()
    private val _ownAccountsLDResponse = MutableLiveData<Result<Response<List<AccountsResponse>>>>()
    private val _operationLD = MutableLiveData<Result<Response<OperationResponse>>>()


    val accountLD: LiveData<Result<Response<AccountsResponse>>> = _accountLD
    val ownAccountsLDResponse: LiveData<Result<Response<List<AccountsResponse>>>> = _ownAccountsLDResponse
    val operationLD: LiveData<Result<Response<OperationResponse>>> = _operationLD

    fun createAccount(token: String, account: AccountsResponse) {
        viewModelScope.launch {
            try {
                val response = accountUseCase.createAccount(token, account)
                _accountLD.postValue(Result.success(response))
            } catch (e: Exception) {
                _accountLD.postValue(Result.failure(e))
            }
        }
    }

    fun getOwnAccounts(token: String) {
        viewModelScope.launch {
            try {
                val response = accountUseCase.getOwnAccounts(token)
                _ownAccountsLDResponse.postValue(Result.success(response))
            } catch (e: Exception) {
                _ownAccountsLDResponse.postValue(null)
            }
        }
    }

    fun transactionOperation(accountId: Int, token: String, request: OperationRequest) {
        viewModelScope.launch {
            try {
                val response = accountUseCase.sendDepositMoney(accountId, token, request)
                if (response.isSuccessful) {
                    _operationLD.postValue(Result.success(response))
                } else {
                    _operationLD.postValue(
                        Result.failure(
                            Exception(
                                response.errorBody()?.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                _operationLD.postValue(Result.failure(e))
            }
        }
    }
}