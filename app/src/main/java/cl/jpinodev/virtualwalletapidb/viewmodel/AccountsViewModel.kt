package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class AccountsViewModel(private val accountUseCase: AccountsUseCase) : ViewModel() {
    private val _accountLD = MutableLiveData<Result<Response<Accounts>>>()
    private val _ownAccountsLD = MutableLiveData<Result<Response<List<Accounts>>>>()
    private val _operationLD = MutableLiveData<Result<Response<OperationResponse>>>()


    val accountLD: LiveData<Result<Response<Accounts>>> = _accountLD
    val ownAccountsLD: LiveData<Result<Response<List<Accounts>>>> = _ownAccountsLD
    val operationLD: LiveData<Result<Response<OperationResponse>>> = _operationLD

    fun createAccount(token: String, account: AccountRequest) {
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
                _ownAccountsLD.postValue(Result.success(response))
            } catch (e: Exception) {
                _ownAccountsLD.postValue(null)
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