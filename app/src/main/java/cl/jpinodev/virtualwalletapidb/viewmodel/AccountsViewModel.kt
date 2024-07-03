package cl.jpinodev.virtualwalletapidb.viewmodel

import android.util.Log
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
    private val _ownAccountsLD = MutableLiveData<Result<List<Accounts>?>>()
    private val _operationLD = MutableLiveData<Result<Response<OperationResponse>>>()


    val accountLD: LiveData<Result<Response<Accounts>>> = _accountLD
    val ownAccountsLD: MutableLiveData<Result<List<Accounts>?>> = _ownAccountsLD
    val operationLD: LiveData<Result<Response<OperationResponse>>> = _operationLD

    fun createAccount(token: String, account: AccountRequest) {
        viewModelScope.launch {
            try {
                val response = accountUseCase.createAccount(token, account)
                if (response.isSuccessful) {
                    response.body()?.let {
                        accountUseCase.saveAccountOnDB(it)
                        _accountLD.postValue(Result.success(response))
                    }
                } else {
                    _accountLD.postValue(Result.failure(Exception("Error: ${response.code()}")))
                }
            } catch (e: Exception) {
                _accountLD.postValue(Result.failure(e))
            }
        }
    }

    fun getOwnAccountsFromApi(token: String) {
        viewModelScope.launch {
            try {
                val response = accountUseCase.getOwnAccounts(token)
                if (response.isSuccessful) {
                    response.body()?.let { accounts ->
                        _ownAccountsLD.postValue(Result.success(accounts))
                        accounts.forEach { account ->
                            accountUseCase.saveAccountOnDB(account)
                        }
                    } ?: _ownAccountsLD.postValue(Result.failure(Exception("Respuesta vacía")))
                } else {
                    _ownAccountsLD.postValue(Result.failure(Exception("Error: ${response.code()}")))
                }
            } catch (e: Exception) {
                _ownAccountsLD.postValue(Result.failure(e))
            }
        }
    }

    fun getOwnAccountsFromDBbyUserId(userId: Int) {
        viewModelScope.launch {
            _ownAccountsLD.postValue(
                try {
                    accountUseCase.getAccountsFromDb(userId)
                } catch (e: Exception) {
                    // Log del error para debugging
                    Log.e("AccountViewModel", "Error al obtener cuentas", e)
                    Result.failure(e)
                }
            )
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