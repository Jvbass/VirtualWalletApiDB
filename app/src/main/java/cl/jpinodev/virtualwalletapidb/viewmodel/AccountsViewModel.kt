package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class AccountsViewModel(private val accountUseCase: AccountsUseCase): ViewModel() {
    private val _accountLD = MutableLiveData<Result<Response<Accounts>>>()
    private val _ownAccountsLD = MutableLiveData<Result<Response<List<Accounts>>>>()


    val accountLD: LiveData<Result<Response<Accounts>>> = _accountLD
    val ownAccountsLD: LiveData<Result<Response<List<Accounts>>>> = _ownAccountsLD

    fun createAccount(token: String, account: Accounts) {
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
}
