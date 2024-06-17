package cl.jpinodev.virtualwalletapidb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.jpinodev.virtualwalletapidb.domain.TransactionsUseCase

class TransactionsViewModelFactory(private val useCase: TransactionsUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}