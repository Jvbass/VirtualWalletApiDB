package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase

class AccountsViewModelFactory(private val accountUseCase: AccountsUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountsViewModel(accountUseCase) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

/*
* class UsersViewModelFactory(private val usersUseCase: UsersUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(usersUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/