package cl.jpinodev.virtualwalletapidb.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cl.jpinodev.virtualwalletapidb.R
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferencesHelper
import cl.jpinodev.virtualwalletapidb.data.local.database.AppDatabase
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.FragmentTransactionSendBinding
import cl.jpinodev.virtualwalletapidb.domain.AccountsUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.AccountsViewModelFactory

class TransactionSend : Fragment() {
    private lateinit var binding: FragmentTransactionSendBinding
    private lateinit var accountsViewModel: AccountsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_send, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTransactionSendBinding.bind(view)
        val navController = Navigation.findNavController(view)


        binding.materialToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        // Configurar AccountsViewModel
        val accountApiService: AccountApiService =
            RetrofitHelper.getRetrofit().create(AccountApiService::class.java)
        val database= AppDatabase.getDatabase(requireContext())

        val accountsRepository = AccountsRepositoryImpl(accountApiService, database.AccountDao())
        val accountsUseCase: AccountsUseCase = AccountsUseCase(accountsRepository)
        val factory = AccountsViewModelFactory(accountsUseCase)
        accountsViewModel = ViewModelProvider(this, factory)[AccountsViewModel::class.java]

        binding.sendButton.setOnClickListener {
            val accountId = binding.toAccountEditText.text.toString()
            val amount = binding.amountEditText.text.toString()
            val message = binding.txtMessage.text.toString()

            if (accountId.isNotEmpty() && amount.isNotEmpty()) {
                val accountId = accountId.toIntOrNull()
                val amount = amount.toIntOrNull()


                if (accountId != null && amount != null && amount > 0) {
                    val token = SharedPreferencesHelper.getToken(requireContext())
                    val request = OperationRequest("payment", message, amount)
                    token?.let {
                        accountsViewModel.transactionOperation(accountId, "Bearer $token", request)
                    }
                } else {
                    ToastUtils.showCustomToast(
                        requireContext(),
                        "Ingrese números válidos para el ID de cuenta y la cantidad."
                    )
                }
            } else {
                ToastUtils.showCustomToast(requireContext(), "Complete los campos requeridos.")
            }
        }

        accountsViewModel.operationLD.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                val message = response.body()?.message
                if (message == "OK") {
                    ToastUtils.showCustomToast(requireContext(), "Operacion Exitosa")
                    navController.navigateUp()
                } else {
                    ToastUtils.showCustomToast(requireContext(), message ?: "Mensaje")
                }
            }

            result.onFailure { throwable ->
                ToastUtils.showCustomToast(requireContext(), "Error: ${throwable.message}")
            }
        })
    }
}