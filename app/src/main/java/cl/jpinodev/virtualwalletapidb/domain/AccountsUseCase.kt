package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepository
import retrofit2.Response

class AccountsUseCase(private val accountRepository: AccountsRepository) {
    suspend fun createAccount(token: String,account: Accounts): Response<Accounts> {
        return accountRepository.createAccount(token,account)
    }
}

/*
*  suspend fun createUser(user: Users): Response<Users> {
        return repository.createUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return repository.loginUser(loginRequest)
    }
* */